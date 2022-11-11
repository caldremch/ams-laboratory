package com.caldremch.asm

/**
 * Created by Leon on 2022/11/10
 */
object ServiceManager {
    private val services = hashMapOf<String, Class<out IService>>()

    fun  serviceInit(){
    }

    fun getService(name:String):IService?{
        return  services[name]?.newInstance()
    }
}