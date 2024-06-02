package com.example.ksptt.ttst

import com.example.kspt.Testt

@Testt("class", data = 100)
open class PacTest32 {
    @Testt("filed, a", data = 90)
    var a = 90

    @Testt("method:ttt", data = 0)
    fun ttt(param1: String) {
        println("000")
    }
}