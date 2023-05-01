package com.kdownloader.internal

import com.kdownloader.Constants
import com.kdownloader.Status
import com.kdownloader.utils.getUniqueId
import kotlinx.coroutines.Job

class DownloadRequest private constructor(
    internal var url: String,
    internal val tag: String?,
    internal var listener: Listener?,
    internal val headers: HashMap<String, List<String>>?,
    internal val dirPath: String,
    internal val downloadId: Int,
    internal val fileName: String,
    internal var status: Status = Status.UNKNOWN,
    internal var readTimeOut: Int = 0,
    internal var connectTimeOut: Int = 0,
    internal var userAgent: String = Constants.DEFAULT_USER_AGENT
) {

    var totalBytes: Long = 0
    var downloadedBytes: Long = 0
    internal lateinit var job: Job

    data class Builder(
        private val url: String, private val dirPath: String, private val fileName: String
    ) {

        private var tag: String? = null
        private var listener: Listener? = null
        private var headers: HashMap<String, List<String>>? = null
        private var readTimeOut: Int = Constants.DEFAULT_READ_TIMEOUT_IN_MILLS
        private var connectTimeOut: Int = Constants.DEFAULT_CONNECT_TIMEOUT_IN_MILLS
        private var userAgent: String = Constants.DEFAULT_USER_AGENT

        fun tag(tag: String) = apply {
            this.tag = tag
        }

        fun headers(headers: HashMap<String, List<String>>) = apply {
            this.headers = headers
        }

        fun readTimeout(timeout: Int) = apply {
            this.readTimeOut = timeout
        }

        fun connectTimeout(timeout: Int) = apply {
            this.connectTimeOut = timeout
        }

        fun userAgent(userAgent: String) = apply {
            this@Builder.userAgent = userAgent
        }

        fun build(): DownloadRequest {
            return DownloadRequest(
                url = url,
                tag = tag,
                listener = listener,
                headers = headers,
                dirPath = dirPath,
                downloadId = getUniqueId(url, dirPath, fileName),
                fileName = fileName,
                readTimeOut = readTimeOut,
                connectTimeOut = connectTimeOut,
                userAgent = userAgent
            )
        }
    }

    interface Listener {
        fun onStart()
        fun onProgress(value: Int)
        fun onPause()
        fun onCompleted()
        fun onError(error: String)
    }

    fun reset(){
        downloadedBytes = 0
        totalBytes = 0
        status = Status.UNKNOWN
    }

}