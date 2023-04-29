package com.kdownloader.internal

import com.kdownloader.Status
import com.kdownloader.utils.getTempPath
import kotlinx.coroutines.*
import java.io.File

class DownloadDispatchers() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main +
            CoroutineExceptionHandler { _, _ ->

            })

    fun enqueue(req: DownloadRequest): Int {
        val job = scope.launch {
            execute(req)
        }
        req.job = job
        return req.downloadId
    }

    private suspend fun execute(request: DownloadRequest) {
        DownloadTask(request).run(
            onStart = {
                executeOnMainThread { request.listener?.onStart() }
            },
            onProgress = {
                executeOnMainThread { request.listener?.onProgress(it) }
            },
            onCompleted = {
                executeOnMainThread { request.listener?.onCompleted() }
            },
            onError = {
                executeOnMainThread { request.listener?.onError(it) }
            }
        )

    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }

    fun cancel(req: DownloadRequest) {

        req.job.cancel()

        if (req.status == Status.PAUSED) {
            val tempPath = getTempPath(req.dirPath, req.fileName)
            val file = File(tempPath)
            if (file.exists()) {
                file.delete()
            }
        }

        req.status = Status.CANCELLED
    }

    fun cancelAll() {
        scope.cancel()
    }
}