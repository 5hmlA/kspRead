package process

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunction
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.symbol.KSPropertySetter
import com.google.devtools.ksp.validate
import log
import toStr

context(SymbolProcessorEnvironment)
fun symbolsWithAnnotation(resolver: Resolver) {
    resolver.getSymbolsWithAnnotation("com.example.kspt.Testt").forEach {
//        logger.log("> symbolsWithAnnotation ${it::class.java.simpleName} ann:${it.annotations.joinToString { it.toStr() }}")
        val node = it
        if (node is KSClassDeclaration) {
            logger.log("KSClassDeclaration ${node.toStr()}")
        } else if (node is KSPropertyDeclaration) {
            logger.log("KSPropertyDeclaration ${node.toStr()}")
        } else if (node is KSPropertyGetter) {
            logger.log("KSPropertyGetter ${node.toStr()}")
        } else if (node is KSPropertySetter) {
            logger.log("KSPropertySetter ${node.toStr()}")
        } else if (node is KSFunction) {
            logger.log("KSFunction ${node.origin}")
        } else if (node is KSFunctionDeclaration) {
            logger.log("KSFunctionDeclaration ${node.toStr()}")
        }
    }

    logger.warn("===================================")
    resolver.getSymbolsWithAnnotation("com.example.kspt.Testt")
        .filter { it.validate() }
        .filterIsInstance<KSClassDeclaration>()
//        .filter { it is KSClassDeclaration }
//        .map { it as KSClassDeclaration }
        .forEach {
            logger.warn("============== ${it.qualifiedName?.asString()} =====================")
            it.getDeclaredProperties().filter { it.annotations.toList().isNotEmpty() }.forEach {
                logger.warn(it.toStr())
            }
        }
}