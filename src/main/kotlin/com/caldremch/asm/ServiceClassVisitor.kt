package com.caldremch.asm

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

/**
 * Created by Leon on 2022/11/10
 */
class ServiceClassVisitor(classVisitor: ClassVisitor?) : ClassVisitor(Opcodes.ASM9, classVisitor) {

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        println("fileName:${name}")
        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("method=$name")
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)
        if ("<clinit>" != name) return vm
//        if ("serviceInit" != name) return vm
        return ServiceMethodVisitor(vm)
    }

}


class ServiceMethodVisitor(methodVisitor: MethodVisitor?) : MethodVisitor(Opcodes.ASM9, methodVisitor) {

    override fun visitEnd() {
        super.visitEnd()
    }


    override fun visitInsn(opcode: Int) {
        if(opcode>=Opcodes.IRETURN && opcode <= Opcodes.RETURN){
            super.visitFieldInsn(GETSTATIC, "com/caldremch/asm/ServiceManager", "services","Ljava/util/HashMap;" )
            super.visitLdcInsn(Type.getType("Lcom/caldremch/asm/IService;"));
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
            super.visitLdcInsn(Type.getType("Lcom/caldremch/asm/ServiceA;"));
            super.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
        }
        super.visitInsn(opcode)


    }

    override fun visitCode() {
        super.visitCode()
    }


}