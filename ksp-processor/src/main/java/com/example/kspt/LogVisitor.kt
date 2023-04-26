package com.example.kspt

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.KSVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import java.util.logging.Logger

class LogVisitor(val logger: KSPLogger, val loger: (String) -> Unit = {}) : KSVisitor<String, Unit> {

    fun log(msg: String) {
        logger.warn(msg)
        loger(msg)
    }


    override fun visitFile(file: KSFile, data: String) {
        val name = file.fileName
        log("visitDeclaration ========= $name =======================================")
        file.accept(this, "visitDeclaration $name > ${file::class.simpleName}")
        log("visitDeclaration --------- $name ----------------------------------------")
    }

    override fun visitDeclaration(declaration: KSDeclaration, data: String) {
        val name = declaration.simpleName.asString()
        log("visitDeclaration ========= $name =======================================")
        declaration.accept(this, "visitDeclaration $name > ${declaration::class.simpleName}")
        log("visitDeclaration --------- $name ----------------------------------------")
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: String) {
        val name = classDeclaration.simpleName.getShortName()
        log("visitClassDeclaration ========= $name =======================================")
        //类里面有成员变量 和 方法
        log("packageName:${classDeclaration.packageName.asString()}  className:${classDeclaration.simpleName.asString()}")
        log("primaryConstructor:${classDeclaration.primaryConstructor?.simpleName?.asString()}  isCompanionObject:$${classDeclaration.isCompanionObject}")
        log("classKind type:${classDeclaration.classKind.type}  classKind name:${classDeclaration.classKind.name}")
        log("modifiers :${classDeclaration.modifiers.joinToString { it.name }}")

//        获取类声明的所有超类型调用getAllSuperTypes需要类型解析，因此代价高昂，应尽可能避免
        log("AllSuperTypes >>> ${classDeclaration.getAllSuperTypes().joinToString { it.declaration.simpleName.getShortName() }}")

        log("visitClassDeclaration  typeParameters ========= $name =======================================")
        //泛型
        classDeclaration.typeParameters.forEach {
            log("""
                [KSTypeParameter] ${it.name.getShortName()} 
                > ${it.bounds.joinToString { it.toString() }}
                > ${it.typeParameters.joinToString { it.name.getShortName() }} 
                > ${it.modifiers.joinToString { it.name }}
            """.trimIndent()
            )
            it.accept(this, "visitClassDeclaration> $name:${it.simpleName.getShortName()}")
        }
        log("visitClassDeclaration  typeParameters ---------- $name ---------------------------------------")

//        获取直接在类声明中声明的函数。
//        包含的内容：成员函数、构造函数、在其内部声明的扩展函数等。不包含的内容：继承的函数、在其外部声明的扩展函数
        classDeclaration.getDeclaredFunctions().forEach {
            log(">[KSFunctionDeclaration] name:${it.simpleName.asString()} >isConstructor:${it.isConstructor()} >return :${it.returnType}")
            it.accept(this, "visitClassDeclaration> $name:${it.simpleName.getShortName()}")
        }

//        获取在类声明中直接声明的属性。
//        包括的内容：成员属性、在其内部声明的扩展属性等。不包括的内容：继承的属性、在其外部声明的扩展属性。
        classDeclaration.getDeclaredProperties().forEach {
            log("[KSPropertyDeclaration] > name:${it.simpleName.asString()} ")
            it.accept(this, "visitClassDeclaration> $name:${it.simpleName.getShortName()}")
        }

        log("visitClassDeclaration --------- $name ----------------------------------------")
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: String) {
        val name = function.simpleName.getShortName()
        log("visitFunctionDeclaration ========= $name =======================================")
        log("isAbstract > ${function.isAbstract}")
        log("modifiers > ${function.modifiers.joinToString { it.toString() }}")
        log("override > ${function.findOverridee()?.simpleName?.asString()}")
        log("returnType > ${function.returnType?.toString()}")
        log("params > ${function.parameters.joinToString { "${it.name?.getShortName()}:${it.type}" }}")
        function.parameters.forEach {
            log("[params] > name:${it.name?.getShortName()} type:${it.type} hasDefault:${it.hasDefault} $it")
            it.accept(this, "visitFunctionDeclaration > $data")
        }
        //泛型
        function.typeParameters.forEach {
            log("""
                [KSTypeParameter] ${it.name.getShortName()} 
                > ${it.bounds.joinToString { it.toString() }}
                > ${it.typeParameters.joinToString { it.name.getShortName() }} 
                > ${it.modifiers.joinToString { it.name }}
            """.trimIndent()
            )
            it.accept(this, "visitFunctionDeclaration> $name:${it.simpleName.getShortName()}")
        }

        log("visitFunctionDeclaration --------- $name ----------------------------------------")
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: String) {
        val name = property.simpleName.getShortName()
        log("visitPropertyDeclaration ========= $name =======================================")
        log("isAbstract > ${property.isAbstract()}")
        log("isMutable 如果此属性是可变的 > ${property.isMutable}")
        log("type 此声明的类型 > ${property.type}")
        log("isDelegated 指示这是否是委托属性 > ${property.isDelegated()}")
        log("modifiers > ${property.modifiers.joinToString { it.toString() }}")
        log("override > ${property.findOverridee()?.simpleName?.asString()}")
        log("visitFunctionDeclaration --------- $name ----------------------------------------")
    }

    override fun visitAnnotated(annotated: KSAnnotated, data: String) {
        log("visitAnnotated ========= ${annotated::class.simpleName} =======================================")
        annotated.annotations.forEach {
            log("visitAnnotated >> annottion ${it.shortName}")
            it.accept(this, data)
        }
        log("visitAnnotated ------------ ${annotated::class.simpleName} -----------------------------------------")
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: String) {
        val name = annotation.shortName
        log("visitPropertyDeclaration ========= $name =======================================")
        log("annotationType: ${annotation.annotationType.toString()}")
        log("arguments: ${annotation.arguments.joinToString { "${it.name}:${it.value}" }}")
        log("defaultArguments: ${annotation.defaultArguments.joinToString { "${it.name}:${it.value}" }}")
        log("visitAnnotation ------------- $name ----------------------------------------")
    }

    override fun visitCallableReference(reference: KSCallableReference, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitClassifierReference(reference: KSClassifierReference, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitDeclarationContainer(declarationContainer: KSDeclarationContainer, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitDefNonNullReference(reference: KSDefNonNullReference, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitDynamicReference(reference: KSDynamicReference, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitModifierListOwner(modifierListOwner: KSModifierListOwner, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitNode(node: KSNode, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitParenthesizedReference(reference: KSParenthesizedReference, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitPropertyAccessor(accessor: KSPropertyAccessor, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitPropertyGetter(getter: KSPropertyGetter, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitPropertySetter(setter: KSPropertySetter, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitReferenceElement(element: KSReferenceElement, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitTypeAlias(typeAlias: KSTypeAlias, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: String) {
        TODO("Not yet implemented")
    }

    override fun visitTypeParameter(typeParameter: KSTypeParameter, data: String) {
//        <R : PacTest32, H : String> //两个 KSTypeParameter   name:R  bound:PacTest32   name:H  bound:String
//        <R : PacTest32>             //一个  KSTypeParameter   name:R  bound:PacTest32
        log("visitTypeParameter ==================${typeParameter.name.getShortName()}===========================================")
        log("modifiers: ${typeParameter.modifiers.joinToString { it.name }}")
        log("annotations: ${typeParameter.annotations.joinToString { it.shortName.getShortName() }}")
        log("typeParameters: ${typeParameter.typeParameters.joinToString { it.name.getShortName() }}")
        log("bounds: ${typeParameter.bounds.joinToString { it.toString() }}") //泛型
        log("visitTypeParameter ------------------${typeParameter.name.getShortName()}-------------------------------------------")
    }

    override fun visitTypeReference(typeReference: KSTypeReference, data: String) {
        log("visitTypeReference ==================${typeReference.toString()}===========================================")
        log("modifiers: ${typeReference.modifiers.joinToString { it.name }}")
        log("annotations: ${typeReference.annotations.joinToString { it.shortName.getShortName() }}")
        log("visitTypeReference ------------------${typeReference.toString()}-------------------------------------------")
    }

    override fun visitValueArgument(valueArgument: KSValueArgument, data: String) {
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: String) {
    }
}