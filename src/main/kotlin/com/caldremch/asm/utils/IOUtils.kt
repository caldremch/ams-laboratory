package com.caldremch.asm.utils

import java.io.*

object IOUtils {
    const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4
    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Int {
        val count = copyLarge(input, output)
        return if (count > Int.MAX_VALUE) {
            -1
        } else count.toInt()
    }

    @Throws(IOException::class)
    fun copyLarge(input: InputStream, output: OutputStream): Long {
        return copy(input, output, DEFAULT_BUFFER_SIZE)
    }

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream, bufferSize: Int): Long {
        return copyLarge(input, output, ByteArray(bufferSize))
    }

    @Throws(IOException::class)
    fun copyLarge(input: InputStream, output: OutputStream, buffer: ByteArray?): Long {
        var count: Long = 0
        var n: Int
        while (EOF != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun copyLarge(
        input: Reader, output: Writer, buffer: CharArray? = CharArray(
            DEFAULT_BUFFER_SIZE
        )
    ): Long {
        var count: Long = 0
        var n: Int
        while (EOF != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    @JvmStatic
    fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (ioe: IOException) {
            // ignore
        }
    }
}