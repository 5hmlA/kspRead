package process

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunction
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.visitor.KSTopDownVisitor
import com.squareup.kotlinpoet.ksp.toTypeName
import log

fun KSFile.toStr(): String {
    return "$fileName ${packageName.asString()} $filePath $annotations"
}

context(SymbolProcessorEnvironment)
fun traverseAllFiles(resolver: Resolver) {
    resolver.getAllFiles().forEach {
        logger.log("====================== start ${it.toStr()}", it)
        it.accept(KSFileVisitor(logger), Unit)
        logger.log("====================== end ${it.toString()}", it)

    }
}

//class KSFileVisitor : KSDefaultVisitor<Unit, Unit>() {
//    override fun defaultHandler(node: KSNode, data: Unit) {
//        TODO("Not yet implemented")
//    }
//}

class KSFileVisitor(val logger: KSPLogger) : KSTopDownVisitor<Unit, Unit>() {
    override fun defaultHandler(node: KSNode, data: Unit) {
        if (node is KSClassDeclaration) {
            logger.log(" KSClassDeclaration ${node.packageName.asString()}->${node.simpleName.asString()} anno:${node.annotations.joinToString { it.shortName.getShortName() }}")
        } else if (node is KSPropertyDeclaration) {
            logger.log("KSPropertyDeclaration ${node.simpleName.asString()} anno:${node.annotations.joinToString { it.shortName.getShortName() }}")
            logger.log("KSPropertyDeclaration ${node.qualifiedName?.asString()} anno:${node.annotations.joinToString { it.shortName.getShortName() }}")
        } else if (node is KSPropertyGetter) {
            logger.log("KSPropertyGetter receiver ${node.receiver.simpleName.asString()} anno:${node.annotations.joinToString { it.shortName.getShortName() }}")
        } else if (node is KSFunction) {
            logger.log("KSFunction ${node.origin}")
        } else if (node is KSFunctionDeclaration) {
            logger.log("KSFunctionDeclaration ${node.simpleName.asString()} ${node.returnType?.toTypeName()} anno:${node.annotations.joinToString { it.shortName.getShortName() }}")
        }

    }

//    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
//        super.visitClassDeclaration(classDeclaration, data)
//        logger.log("visitClassDeclaration  ${classDeclaration.simpleName.asString()} ${classDeclaration.annotations.joinToString { it.shortName.asString() }}")
//        classDeclaration.getDeclaredFunctions().forEach {
//            logger.log("visitClassDeclaration getDeclaredFunctions ${it.simpleName.asString()}  ${it.annotations.joinToString { it.shortName.getShortName() }}")
//        }
//        classDeclaration.getAllFunctions().forEach {
//            logger.log("visitClassDeclaration getAllFunctions ${it.simpleName.asString()}")
//        }
//    }

//    override fun visitAnnotated(annotated: KSAnnotated, data: Unit) {
//        super.visitAnnotated(annotated, data)
//        logger.log("visitAnnotated ${annotated.annotations.joinToString { it.shortName.asString() }}")
//        annotated.annotations.forEach {
//            logger.log("     ${it.arguments.toStr()}")
//        }
//    }
}

