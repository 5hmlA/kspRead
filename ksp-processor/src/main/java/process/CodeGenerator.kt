package process

import appendText
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import log
import toStr

context(SymbolProcessorEnvironment)
fun codeGenerator(resolver: Resolver) {
    symbolsWithAnnotation(resolver)
    logger.log(">>>>>>>>>  ${codeGenerator.generatedFile.toStr()} >>>>>>>>")
    try {
        val kt = codeGenerator.createNewFile(Dependencies(false), "com.pkg", "KSPCenerated", "kt")
        kt.appendText(
            """
                package com.pkg
                
                class KSPCenerated {
                    val filed_1: Int = 110
                    
                    fun method_1(msg: String) {
                        println("log log log")
                    }
                }
            """.trimIndent()
        )
    } catch (e: Exception) {
        //一次(可能多轮)ksp只能生成一次同样的文件
        //新执行(项目变化新执行编译)的ksp允许覆盖，不过增量应该不存在此场景
        logger.log(">>>>>>>>>  codeGenerator xxx ${e.message} >>>>>>>>")

        val file_path = """com\pkg\KSPCenerated"""
        codeGenerator.associateByPath(emptyList(), file_path)
    }

    try {
        //文件写入 resources/META-INF/services
        val resourceFile = "META-INF/services/providerInterface"
        val kt = codeGenerator.createNewFile(Dependencies(false), "", resourceFile, "")
        kt.appendText(
            """
                    package com.pkg
                    
                    ${System.currentTimeMillis()}
                """.trimIndent()
        )
    } catch (e: Exception) {
        e.printStackTrace()
        logger.log("xxxxxxxxxxxxxxxxxxx ${e.message}")
    }

    //获取所有新增的文件 包括ksp生成的和项目新增的
    resolver.getNewFiles().forEach {
        logger.log(">>>>>>>>> getNewFiles >> ${it.fileName} >>>>>>>> ")
    }

    logger.log(">>>>>>>>>  ${codeGenerator.generatedFile.toStr()} >>>>>>>>")
    logger.log(">>>>>>>>>  codeGenerator >>>>>>>> ")
}