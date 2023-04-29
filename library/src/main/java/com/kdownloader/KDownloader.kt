package com.kdownloader

import android.content.Context
import com.kdownloader.database.AppDbHelper
import com.kdownloader.database.DbHelper
import com.kdownloader.database.NoOpsDbHelper
import com.kdownloader.internal.DownloadDispatchers
import com.kdownloader.internal.DownloadRequest
import com.kdownloader.internal.DownloadRequestQueue

class KDownloader private constructor(dbHelper: DbHelper) {

    companion object {
        fun create(
            context: Context,
            config: DownloaderConfig = DownloaderConfig(true)
        ): KDownloader {
            return if (config.databaseEnabled) {
                KDownloader(AppDbHelper(context))
            } else {
                KDownloader(NoOpsDbHelper())
            }
        }
    }

    private val downloader = DownloadDispatchers(dbHelper)
    private val reqQueue = DownloadRequestQueue(downloader)

    fun newRequestBuilder(url: String, dirPath: String, fileName: String): DownloadRequest.Builder {
        return DownloadRequest.Builder(url, dirPath, fileName)
    }

    fun enqueue(req: DownloadRequest, listener: DownloadRequest.Listener): Int {
        req.listener = listener
        return reqQueue.enqueue(req)
    }

    inline fun enqueue(
        req: DownloadRequest,
        crossinline onStart: () -> Unit = {},
        crossinline onProgress: (value: Int) -> Unit = { _ -> },
        crossinline onError: (error: String) -> Unit = { _ -> },
        crossinline onCompleted: () -> Unit = {}
    ) = enqueue(req, object : DownloadRequest.Listener {
        override fun onStart() = onStart()
        override fun onProgress(value: Int) = onProgress(value)
        override fun onError(error: String) = onError(error)
        override fun onCompleted() = onCompleted()
    })

    fun status(id: Int): Status {
        return reqQueue.status(id)
    }

    fun cancel(id: Int) {
        reqQueue.cancel(id)
    }

    fun cancel(tag: String) {
        reqQueue.cancel(tag)
    }

    fun cancelAll() {
        reqQueue.cancelAll()
    }

    fun pause(id: Int) {
        reqQueue.pause(id)
    }

    fun resume(id: Int) {
        reqQueue.resume(id)
    }

    fun cleanUp(days: Int) {
        downloader.cleanup(days)

    }

}