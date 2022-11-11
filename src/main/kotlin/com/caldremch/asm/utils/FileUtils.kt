package com.caldremch.asm.utils

import com.caldremch.asm.utils.IOUtils.closeQuietly
import com.caldremch.asm.utils.IOUtils.copy
import java.io.*
import java.nio.charset.StandardCharsets

object FileUtils {
    var name = "FileUtils"
    fun getFilePath(relativePath: String): String {
        val dir = FileUtils::class.java.getResource("/").path
        return dir + relativePath
    }

    fun getFilePath(clazz: Class<*>, className: String): String {
        val path = clazz.getResource("/").path
        return String.format("%s%s.class", path, className.replace('.', File.separatorChar))
    }

    fun readBytes(filepath: String): ByteArray {
        val file = File(filepath)
        require(file.exists()) { "File Not Exist: $filepath" }
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(file)
            `in` = BufferedInputStream(`in`)
            val bao = ByteArrayOutputStream()
            copy(`in`, bao)
            return bao.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeQuietly(`in`)
        }
        throw RuntimeException("Can not read file: $filepath")
    }

    fun writeBytes(filepath: String, bytes: ByteArray?) {
        val file = File(filepath)
        val dirFile = file.parentFile
        mkdirs(dirFile)
        try {
            FileOutputStream(filepath).use { out ->
                BufferedOutputStream(out).use { buff ->
                    buff.write(bytes)
                    buff.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (Const.DEBUG) println("file://$filepath")
    }

    @JvmOverloads
    fun readLines(filepath: String, charsetName: String? = "UTF8"): List<String>? {
        val file = File(filepath)
        require(file.exists()) { "File Not Exist: $filepath" }
        var `in`: InputStream? = null
        var reader: Reader? = null
        var bufferReader: BufferedReader? = null
        try {
            `in` = FileInputStream(file)
            reader = InputStreamReader(`in`, charsetName)
            bufferReader = BufferedReader(reader)
            val list: MutableList<String> = ArrayList()
            var line: String
            while (bufferReader.readLine().also { line = it } != null) {
                list.add(line)
            }
            return list
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeQuietly(bufferReader)
            closeQuietly(reader)
            closeQuietly(`in`)
        }
        assert(!Const.DEBUG) { "bytes is null" }
        return null
    }

    fun writeLines(filepath: String?, lines: List<String?>?) {
        if (lines == null || lines.size < 1) return
        val file = File(filepath)
        val dirFile = file.parentFile
        mkdirs(dirFile)
        var out: OutputStream? = null
        var writer: Writer? = null
        var bufferedWriter: BufferedWriter? = null
        try {
            out = FileOutputStream(file)
            writer = OutputStreamWriter(out, StandardCharsets.UTF_8)
            bufferedWriter = BufferedWriter(writer)
            for (line in lines) {
                bufferedWriter.write(line)
                bufferedWriter.newLine()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            closeQuietly(bufferedWriter)
            closeQuietly(writer)
            closeQuietly(out)
        }
    }

    fun mkdirs(dirFile: File) {
        val file_exists = dirFile.exists()
        if (file_exists && dirFile.isDirectory) {
            return
        }
        if (file_exists && dirFile.isFile) {
            throw RuntimeException("Not A Directory: $dirFile")
        }
        if (!file_exists) {
            val flag = dirFile.mkdirs()
            assert(!Const.DEBUG || flag) { "Create Directory Failed: " + dirFile.absolutePath }
        }
    }

    fun clear(file: File) {
        if (!file.exists()) {
            return
        }
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (f in files) {
                    delete(f)
                }
            }
        } else {
            delete(file)
        }
    }

    fun delete(file: File) {
        if (!file.exists()) {
            return
        }
        if (file.isFile) {
            val flag = file.delete()
            assert(!Const.DEBUG || flag) { "[Warning] delete file failed: " + file.absolutePath }
        }
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (f in files) {
                    delete(f)
                }
            }
            val flag = file.delete()
            assert(!Const.DEBUG || flag) { "[Warning] delete file failed: " + file.absolutePath }
        }
    }

    fun readStream(`in`: InputStream?, close: Boolean): ByteArray? {
        requireNotNull(`in`) { "inputStream is null!!!" }
        try {
            val out = ByteArrayOutputStream()
            copy(`in`, out)
            return out.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (close) {
                closeQuietly(`in`)
            }
        }
        return null
    }

    fun getInputStream(className: String): InputStream {
        return ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class")
    }
}