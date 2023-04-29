package com.kdownloader.internal

import com.kdownloader.Status
import com.kdownloader.httpclient.DefaultHttpClient
import com.kdownloader.httpclient.HttpClient
import com.kdownloader.internal.stream.FileDownloadOutputStream
import com.kdownloader.internal.stream.FileDownloadRandomAccessFile
import com.kdownloader.utils.getPath
import com.kdownloader.utils.getRedirectedConnectionIfAny
import com.kdownloader.utils.getTempPath
import com.kdownloader.utils.renameFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection

class DownloadTask(
    private val req: DownloadRequest
) {

    private var responseCode = 0
    private var totalBytes: Long = 0
    private var inputStream: InputStream? = null
    private lateinit var outputStream: FileDownloadOutputStream

    private var tempPath: String = ""
    private lateinit var httpClient: HttpClient
    private var isResumeSupported = true

    private var lastSyncTime: Long = 0
    private var lastSyncBytes: Long = 0

    companion object {
        private const val TIME_GAP_FOR_SYNC: Long = 2000
        private const val MIN_BYTES_FOR_SYNC: Long = 65536
        private const val BUFFER_SIZE = 1024 * 4
    }

    suspend inline fun run(
        crossinline onStart: () -> Unit = {},
        crossinline onProgress: (value: Int) -> Unit = { _ -> },
        crossinline onError: (error: String) -> Unit = { _ -> },
        crossinline onCompleted: () -> Unit = {}
    ) = run(object : DownloadRequest.Listener {
        override fun onStart() = onStart()

        override fun onProgress(value: Int) = onProgress(value)

        override fun onError(error: String) = onError(error)

        override fun onCompleted() = onCompleted()
    })

    suspend fun run(listener: DownloadRequest.Listener) {

        withContext(Dispatchers.IO) {
            try {
                tempPath = getTempPath(req.dirPath, req.fileName)
                val file = File(tempPath)

                // use the url to download the file with HTTP Client
                httpClient = DefaultHttpClient().clone()

                req.status = Status.RUNNING

                listener.onStart()

                httpClient.connect(req)

                httpClient = getRedirectedConnectionIfAny(httpClient, req)
                responseCode = httpClient.getResponseCode()

                if (!isSuccessful()) {
                    listener.onError("Wrong link")
                }

                setResumeSupportedOrNot()

                totalBytes = req.totalBytes

                if (!isResumeSupported) {
                    deleteTempFile()
                    req.downloadedBytes = 0
                }

                if (totalBytes == 0L) {
                    totalBytes = httpClient.getContentLength()
                    req.totalBytes = (totalBytes)
                }

                inputStream = httpClient.getInputStream()
                if (inputStream == null) {
                    return@withContext
                }

                val buff = ByteArray(BUFFER_SIZE)

                if (!file.exists()) {
                    val parentFile = file.parentFile
                    if (parentFile != null && !parentFile.exists()) {
                        if (parentFile.mkdirs()) {
                            file.createNewFile()
                        }
                    } else {
                        file.createNewFile()
                    }
                }

                this@DownloadTask.outputStream = FileDownloadRandomAccessFile.create(file)

                if (req.status === Status.CANCELLED) {
                    deleteTempFile()
                    return@withContext
                } else if (req.status === Status.PAUSED) {
                    sync(outputStream)
                    return@withContext
                }

                if (isResumeSupported && req.downloadedBytes != 0L) {
                    outputStream.seek(req.downloadedBytes)
                }

                do {
                    val byteCount = inputStream!!.read(buff, 0, BUFFER_SIZE)
                    if (byteCount == -1) {
                        break
                    }

                    if (req.status === Status.CANCELLED) {
                        deleteTempFile()
                        return@withContext
                    } else if (req.status === Status.PAUSED) {
                        sync(outputStream)
                        return@withContext
                    }

                    if (!isActive) {
                        break
                    }
                    if (!req.job.isActive) {
                        break
                    }
                    outputStream.write(buff, 0, byteCount)
                    req.downloadedBytes = req.downloadedBytes + byteCount
                    withContext(Dispatchers.IO) {
                        syncIfRequired(outputStream)
                    }

                    listener.onProgress(((req.downloadedBytes * 100) / totalBytes).toInt())

                } while (true)

                if (req.status === Status.CANCELLED) {
                    deleteTempFile()
                    return@withContext
                } else if (req.status === Status.PAUSED) {
                    sync(outputStream)
                    return@withContext
                }

                val path = getPath(req.dirPath, req.fileName)
                renameFileName(tempPath, path)
                listener.onCompleted()
                req.status = Status.COMPLETED
                return@withContext
            } catch (e: IllegalAccessException) {
                if (!isResumeSupported) {
                    deleteTempFile()
                }
                listener.onError(e.toString())
                req.status = Status.FAILED
                return@withContext
            } catch (e: IOException) {
                if (!isResumeSupported) {
                    deleteTempFile()
                }
                req.status = Status.FAILED
                listener.onError(e.toString())
                return@withContext
            } finally {
                closeAllSafely(outputStream)
            }
        }
    }

    private fun setResumeSupportedOrNot() {
        isResumeSupported = (responseCode == HttpURLConnection.HTTP_PARTIAL)
    }

    private fun deleteTempFile(): Boolean {
        val file = File(tempPath)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

    private fun closeAllSafely(outputStream: FileDownloadOutputStream) {

        try {
            httpClient.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            inputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            sync(outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun syncIfRequired(outputStream: FileDownloadOutputStream) {
        val currentBytes: Long = req.downloadedBytes
        val currentTime = System.currentTimeMillis()
        val bytesDelta: Long = currentBytes - lastSyncBytes
        val timeDelta: Long = currentTime - lastSyncTime
        if (bytesDelta > MIN_BYTES_FOR_SYNC && timeDelta > TIME_GAP_FOR_SYNC) {
            sync(outputStream)
            lastSyncBytes = currentBytes
            lastSyncTime = currentTime
        }
    }

    private fun sync(outputStream: FileDownloadOutputStream) {
        try {
            outputStream.flushAndSync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun isSuccessful(): Boolean {
        return (responseCode >= HttpURLConnection.HTTP_OK
                && responseCode < HttpURLConnection.HTTP_MULT_CHOICE)
    }
}