package com.caldremch.asm

import com.caldremch.asm.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

fun main(args: Array<String>) {
    val relativePath = "com/caldremch/asm/ServiceManager.class"
    val filePath = FileUtils.getFilePath(relativePath);
    println("filePath=$filePath")
    val bytes = FileUtils.readBytes(filePath)
    val cr = ClassReader(bytes)
    val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    val cv = ServiceClassVisitor(cw)
    //（4）结合ClassReader和ClassVisitor
    val parsingOptions = ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES
    cr.accept(cv, 0)
    val bytesOut = cw.toByteArray()
    FileUtils.writeBytes(filePath, bytesOut)
    ServiceManager.getService(IService::class.java.name)?.showName()


}