package com.kdownloader.httpclient

import com.kdownloader.internal.DownloadRequest
import java.io.IOException
import java.io.InputStream

interface HttpClient : Cloneable {

    fun getContentLength(): Long

    fun getHeaderFields(): Map<String, List<String>>

    @Throws(IOException::class)
    fun getResponseCode(): Int

    @Throws(IOException::class)
    fun getInputStream(): InputStream?

    fun getErrorStream(): InputStream?

    public override fun clone(): HttpClient

    @Throws(IOException::class)
    fun connect(req: DownloadRequest)

    fun getResponseHeader(name: String): String

    fun close()

}