package com.kdownloader.internal

import com.kdownloader.Status

class DownloadRequestQueue(private val downloader: DownloadDispatchers) {

    private val idRequestMap: HashMap<Int, DownloadRequest> = hashMapOf()

    fun enqueue(request: DownloadRequest): Int {
        idRequestMap[request.downloadId] = request
        return downloader.enqueue(request)
    }

    fun status(id: Int): Status {
        val req = idRequestMap[id] ?: return Status.UNKNOWN
        return req.status
    }

    fun cancel(id: Int) {
        val req = idRequestMap[id]
        if (req != null && req.status != Status.CANCELLED) {
            downloader.cancel(req)
        }
        idRequestMap.remove(id)
    }

    fun cancel(tag: String) {
        val list = idRequestMap.values.filter {
            it.tag == tag
        }

        for (req in list) {
            cancel(req.downloadId)
        }
    }

    fun cancelAll() {
        idRequestMap.clear()
        downloader.cancelAll()
    }

    fun pause(id: Int) {
        val req = idRequestMap[id] ?: return
        req.status = Status.PAUSED
    }

    fun resume(id: Int) {
        val req = idRequestMap[id] ?: return
        req.status = Status.QUEUED
        downloader.enqueue(req)
    }
}