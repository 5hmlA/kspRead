package com.example.jvm_lib

import com.example.kspt.Testt

class MyClass {
    @Testt("method:ttt", data = 0)
    fun ttt(param1: String) {
        println("000")
    }
}


@Testt("class", data = 100)
class PacTest32 {
    @Testt("filed, a", data = 90)
    var a = 90

    @Testt("method:ttt", data = 0)
    fun ttt(param1: String) {
        println("000")
    }
}