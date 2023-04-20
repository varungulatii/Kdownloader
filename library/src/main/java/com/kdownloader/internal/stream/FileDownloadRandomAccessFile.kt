package com.library.internal.stream

import java.io.*

class FileDownloadRandomAccessFile private constructor(file: File) : FileDownloadOutputStream {
    private val out: BufferedOutputStream
    private val fd: FileDescriptor
    private val randomAccess: RandomAccessFile

    init {
        randomAccess = RandomAccessFile(file, "rw")
        fd = randomAccess.fd
        out = BufferedOutputStream(FileOutputStream(randomAccess.fd))
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray?, off: Int, len: Int) {
        out.write(b, off, len)
    }

    @Throws(IOException::class)
    override fun flushAndSync() {
        out.flush()
        fd.sync()
    }

    @Throws(IOException::class)
    override fun close() {
        out.close()
        randomAccess.close()
    }

    @Throws(IOException::class)
    override fun seek(offset: Long) {
        randomAccess.seek(offset)
    }

    @Throws(IOException::class)
    override fun setLength(newLength: Long) {
        randomAccess.setLength(newLength)
    }

    companion object {
        @Throws(IOException::class)
        fun create(file: File): FileDownloadOutputStream {
            return FileDownloadRandomAccessFile(file)
        }
    }
}