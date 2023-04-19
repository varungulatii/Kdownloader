package com.kdownloader.httpclient

import com.kdownloader.Constants
import com.kdownloader.internal.DownloadRequest
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.*

class DefaultHttpClient : HttpClient {
    private var connection: URLConnection? = null
    override fun clone(): HttpClient {
        return DefaultHttpClient()
    }

    @Throws(IOException::class)
    override fun connect(req: DownloadRequest) {
        connection = URL(req.url).openConnection()
        connection?.readTimeout = req.readTimeOut
        connection?.connectTimeout = req.connectTimeOut
        val range: String = java.lang.String.format(
            Locale.ENGLISH,
            "bytes=%d-", req.downloadedBytes
        )
        connection!!.addRequestProperty(Constants.RANGE, range)
        connection!!.addRequestProperty(Constants.USER_AGENT, req.userAgent)
        addHeaders(req)
        connection!!.connect()
    }

    @Throws(IOException::class)
    override fun getResponseCode(): Int {
        var responseCode = 0
        if (connection is HttpURLConnection) {
            responseCode = (connection as HttpURLConnection).responseCode
        }
        return responseCode
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream? {
        return connection!!.getInputStream()
    }

    override fun getContentLength(): Long {
        val length: String? = connection!!.getHeaderField("Content-Length")
        return length?.toLong() ?: -1
    }

    override fun getResponseHeader(name: String): String? {
        return connection!!.getHeaderField(name)
    }

    override fun close() {
        // no operation
    }

    override fun getHeaderFields(): Map<String, List<String>> {
        return connection!!.headerFields
    }

    override fun getErrorStream(): InputStream? {
        return if (connection is HttpURLConnection) {
            (connection as HttpURLConnection).errorStream
        } else null
    }

    private fun addHeaders(req: DownloadRequest) {
        val headers: HashMap<String, List<String>>? = req.headers
        if (headers != null) {
            val entries: Set<Map.Entry<String, List<String>>> = headers.entries
            for ((name, list) in entries) {
                for (value in list) {
                    connection!!.addRequestProperty(name, value)
                }
            }
        }
    }
}