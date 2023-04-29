package com.kdownloader.internal.stream

import java.io.IOException

interface FileDownloadOutputStream {
    /**
     * Writes `len` bytes from the specified byte array
     * starting at offset `off` to this file.
     */
    @Throws(IOException::class)
    fun write(b: ByteArray?, off: Int, len: Int)

    /**
     * Flush all buffer to system and force all system buffers to synchronize with the underlying
     * device.
     */
    @Throws(IOException::class)
    fun flushAndSync()

    /**
     * Closes this output stream and releases any system resources associated with this stream. The
     * general contract of `close` is that it closes the output stream. A closed stream
     * cannot perform output operations and cannot be reopened.
     */
    @Throws(IOException::class)
    fun close()

    /**
     * Sets the file-pointer offset, measured from the beginning of this file, at which the next
     * read or write occurs.  The offset may be set beyond the end of the file.
     */
    @Throws(IOException::class, IllegalAccessException::class)
    fun seek(offset: Long)

    /**
     * Sets the length of this file.
     */
    @Throws(IOException::class, IllegalAccessException::class)
    fun setLength(newLength: Long)
}