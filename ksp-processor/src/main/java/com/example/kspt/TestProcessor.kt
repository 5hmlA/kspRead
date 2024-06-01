import com.example.kspt.LogVisitor
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

class TestProcessor(
    val codeGenerator: CodeGenerator,
    val options: Map<String, String>,
    val logger: KSPLogger
) : SymbolProcessor {
    lateinit var file: OutputStream
    var invoked = false

    fun emit(s: String, indent: String = "") {
        file.appendText("$indent   $s\n")
    }

    fun log(msg: String) {
        logger.warn("======== $msg")
    }

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        file = codeGenerator.createNewFile(Dependencies(false), "", "TestProcessor", "log")
        emit("TestProcessor: init($options)", "")

        val javaFile = codeGenerator.createNewFile(Dependencies(false), "", "Generated", "java")
        javaFile.appendText("class Generated {}")

        val fileKt = codeGenerator.createNewFile(Dependencies(false), "com.test", "HELLO", "kt")
        fileKt.appendText(
            """
            package com.example.kspt
            import com.example.kspt.Testt
            
            class Testksp{

                @Testt
                fun testAnnInTest():String{ return "" }
                @Testt
                fun testAnnInTest2(name:String){ }
            }
        """.trimIndent()
        )
        //找到Testt注解的所有内容 包括类/成员变量/方法
        resolver.getSymbolsWithAnnotation("com.example.kspt.Testt").forEach {
            logger.warn("======== annotation ==${it::class.simpleName} ${it.toString()}")
            //遍历所有注解
            it.annotations.forEach {
                //注解名字
                log(" annotation ---  ${it.shortName.getShortName()}")
                //遍历注解的参数
                it.arguments.forEach {
                    log(" annotation -- param ${it.name?.getShortName()} ${it.value}")
                }
            }
            if (it is KSFunctionDeclaration) {
                //被注解的对象是 方法
                val ksFunctionDeclaration = it
                log(ksFunctionDeclaration.returnType.toString())
                log("override ${ksFunctionDeclaration.findOverridee()}")
                ksFunctionDeclaration.parameters.forEach {
                    log("param  ${it.type}")
                }
                ksFunctionDeclaration.typeParameters.forEach {
                    log(it.toString())
                }
                //方法的修饰符 OPEN  PRIVATE  LATEINIT
//                it.modifiers
            } else if (it is KSPropertyDeclaration) {
                //被注解的对象是 property也就是成员变量
                it.modifiers.forEach {
                    //变量 的修饰符 OPEN  PRIVATE  LATEINIT
                    log("modifiers  ${it.name}")
                }
                log("name ${it.simpleName.getShortName()} paname ${it.packageName.getShortName()}  ${it.type}")
            } else if (it is KSClassDeclaration) {
                //被注解的对象是 类
                //类的修饰符 OPEN PRIVATE
                it.modifiers.forEach {
                    log("modifiers  ${it.name}")
                }
                log("name ${it.simpleName.getShortName()} paname ${it.packageName.getShortName()}  ${it.qualifiedName?.getShortName()}")
            }
        }
        //根据包名 检索所有文件
        resolver.getDeclarationsFromPackage("com.example.ksptt.ttst").forEach {
            log("under package  ${it::class} $it  ${it.simpleName.getShortName()}")
        }
        //所有文件
        val files = resolver.getAllFiles()
        emit("TestProcessor: process()", "")
        logger.warn("========file == $this")
        val visitor = TestVisitor()
//        val visitor = LogVisitor(logger)
        for (file in files) {
            emit("********************************* file ********************************* ${file.fileName}")
            emit("TestProcessor: processing ${file.fileName}", "")
            file.accept(visitor, "file")
            emit("################################# file ################################# ${file.fileName}")
        }
        invoked = true

        resolver.getNewFiles().forEach {
            log("new file ============${it.fileName}")
        }
        resolver.getDeclarationsFromPackage("com.example.ksptt.ttst").first().accept(LogVisitor(logger), "")

        return resolver.getSymbolsWithAnnotation("com.example.kspt.Testt").toList()
    }

    //访问者模式 可遍历 文件 类 方法 成员变量
    inner class TestVisitor : KSVisitor<String, Unit> {

        override fun visitReferenceElement(element: KSReferenceElement, data: String) {
        }

        override fun visitModifierListOwner(modifierListOwner: KSModifierListOwner, data: String) {
            TODO("Not yet implemented")
        }

        override fun visitDefNonNullReference(reference: KSDefNonNullReference, data: String) {
            TODO("Not yet implemented")
        }

        override fun visitNode(node: KSNode, data: String) {
            TODO("Not yet implemented")
        }

        override fun visitPropertyAccessor(accessor: KSPropertyAccessor, data: String) {
            TODO("Not yet implemented")
        }

        override fun visitDynamicReference(reference: KSDynamicReference, data: String) {
            TODO("Not yet implemented")
        }

        val visited = HashSet<Any>()

        private fun checkVisited(symbol: Any): Boolean {
            return if (visited.contains(symbol)) {
                true
            } else {
                visited.add(symbol)
                false
            }
        }

        private fun invokeCommonDeclarationApis(declaration: KSDeclaration, indent: String) {
            emit(
                "${declaration.modifiers.joinToString(" ")} ${declaration.simpleName.asString()}", indent
            )
            declaration.annotations.forEach { it.accept(this, "$indent  ") }
            if (declaration.parentDeclaration != null)
                emit("  enclosing: ${declaration.parentDeclaration!!.qualifiedName?.asString()}", indent)
            declaration.containingFile?.let { emit("${it.packageName.asString()}.${it.fileName}", indent) }
            declaration.typeParameters.forEach { it.accept(this, "$indent  ") }
        }

        override fun visitFile(file: KSFile, data: String) {
            //访问文件
            if (checkVisited(file)) return
            emit("visitFile  ==============================================================", data)
            file.annotations.forEach {
                logger.warn("--annotations--------- ${it.shortName.asString()}")
            }
            file.declarations.forEach {
                logger.warn("--declarations--------- ${it.simpleName.asString()}")
            }
            file.annotations.forEach { it.accept(this, "visitFile annotation on file ") }
            emit("visitFile packageName ${file.packageName.asString()}", data)
            for (declaration in file.declarations) {
                emit("visitFile ============= ${declaration.simpleName.getShortName()}===================", data)
                declaration.accept(this, "visitFile ClassDeclaration ")
                emit("visitFile ------------- ${declaration.simpleName.getShortName()}--------------------", data)
            }
            emit("visitFile  -----------------------------------------------------------------", data)
        }

        override fun visitAnnotation(annotation: KSAnnotation, data: String) {
            if (checkVisited(annotation)) return
            emit("visitAnnotation ==============================", data)
            annotation.annotationType.accept(this, " visitCallableReference  ")
            annotation.arguments.forEach { it.accept(this, "visitCallableReference  ") }
            emit("visitAnnotation ---------------------------------", data)
        }

        override fun visitCallableReference(reference: KSCallableReference, data: String) {
            if (checkVisited(reference)) return
            emit("visitCallableReference ==========================================: ", data)
            reference.functionParameters.forEach { it.accept(this, "visitCallableReference  ") }
            reference.receiverType?.accept(this, "visitCallableReference ")
            reference.returnType.accept(this, "visitCallableReference")
            emit("visitCallableReference -------------------------------------------: ", data)
        }

        override fun visitPropertyGetter(getter: KSPropertyGetter, data: String) {
            if (checkVisited(getter)) return
            emit("visitPropertyGetter ==================================================: ", data)
            getter.annotations.forEach { it.accept(this, "visitPropertyGetter") }
            emit(getter.modifiers.joinToString(" "), "visitPropertyGetter")
            getter.returnType?.accept(this, "visitPropertyGetter  ")
            emit("visitPropertyGetter ---------------------------------------------------: ", data)
        }

        override fun visitPropertySetter(setter: KSPropertySetter, data: String) {
            if (checkVisited(setter)) return
            emit("visitPropertySetter ====================================================== ", data)
            setter.annotations.forEach { it.accept(this, "visitPropertySetter ") }
            emit(setter.modifiers.joinToString(" "), "visitPropertySetter  ")
            emit("visitPropertySetter ------------------------------------------------------- ", data)
        }

        override fun visitTypeArgument(typeArgument: KSTypeArgument, data: String) {
            if (checkVisited(typeArgument)) return
            emit("visitTypeArgument ====================================================== ", data)
            typeArgument.annotations.forEach { it.accept(this, "visitTypeArgument ") }
            emit(
                when (typeArgument.variance) {
                    Variance.STAR -> "*"
                    Variance.COVARIANT -> "out"
                    Variance.CONTRAVARIANT -> "in"
                    else -> ""
                }, "visitTypeArgument"
            )
            typeArgument.type?.accept(this, "visitTypeArgument ")
            emit("visitTypeArgument -------------------------------------------------------- ", data)
        }

        override fun visitTypeParameter(typeParameter: KSTypeParameter, data: String) {
            if (checkVisited(typeParameter)) return
            emit("visitTypeParameter ====================================================== ", data)
            typeParameter.annotations.forEach { it.accept(this, "$data  ") }
            if (typeParameter.isReified) {
                emit("visitTypeParameter reified ", "visitTypeParameter")
            }
            emit(
                when (typeParameter.variance) {
                    Variance.COVARIANT -> "out "
                    Variance.CONTRAVARIANT -> "in "
                    else -> ""
                } + typeParameter.name.asString(), "visitTypeParameter"
            )
            if (typeParameter.bounds.toList().isNotEmpty()) {
                typeParameter.bounds.forEach { it.accept(this, "visitTypeParameter  ") }
            }
            emit("visitTypeParameter ---------------------------------------------------------- ", data)
        }

        override fun visitValueParameter(valueParameter: KSValueParameter, data: String) {
            if (checkVisited(valueParameter)) return
            emit("visitValueParameter ===============================================", "$data  ")
            valueParameter.annotations.forEach { it.accept(this, "visitValueParameter  ") }
            if (valueParameter.isVararg) {
                emit("visitTypeParameter vararg", "visitValueParameter  ")
            }
            if (valueParameter.isNoInline) {
                emit("visitTypeParameter noinline", "visitValueParameter  ")
            }
            if (valueParameter.isCrossInline) {
                emit("visitTypeParameter crossinline ", "visitValueParameter  ")
            }
            valueParameter.type.accept(this, "visitValueParameter  ")
            emit("visitValueParameter -----------------------------------------------------", "$data  ")
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: String) {
            if (checkVisited(function)) return
            emit("visitFunctionDeclaration ===============================================", "$data  ")
            invokeCommonDeclarationApis(function, data)
            for (declaration in function.declarations) {
                declaration.accept(this, "visitFunctionDeclaration  ")
            }
            function.parameters.forEach { it.accept(this, "visitFunctionDeclaration  ") }
            function.typeParameters.forEach { it.accept(this, "visitFunctionDeclaration  ") }
            function.extensionReceiver?.accept(this, "visitFunctionDeclaration extension:")
            emit("visitFunctionDeclaration returnType:", data)
            function.returnType?.accept(this, "visitFunctionDeclaration  ")
            emit("visitFunctionDeclaration ----------------------------------------------------", "$data  ")
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: String) {
            emit("visitClassDeclaration ================== ${classDeclaration.simpleName.asString()}=============================", "$data  ")
            if (checkVisited(classDeclaration)) return
            invokeCommonDeclarationApis(classDeclaration, data)
            emit(classDeclaration.classKind.type, data)
            for (declaration in classDeclaration.declarations) {
                declaration.accept(this, "$data ")
            }
            classDeclaration.superTypes.forEach { it.accept(this, "$data  ") }
            classDeclaration.primaryConstructor?.accept(this, "$data  ")
            emit("visitClassDeclaration -------------------- ${classDeclaration.simpleName.asString()}--------------------------------", "$data  ")
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: String) {
            emit("visitPropertyDeclaration ===============================================", "$data  ")
            if (checkVisited(property)) return
            invokeCommonDeclarationApis(property, data)
            property.type.accept(this, "visitPropertyDeclaration  ")
            property.extensionReceiver?.accept(this, "visitPropertyDeclaration extension:")
            property.setter?.accept(this, "visitPropertyDeclaration  ")
            property.getter?.accept(this, "visitPropertyDeclaration  ")
            emit("visitPropertyDeclaration --------------------------------------------------", "$data  ")
        }

        override fun visitTypeReference(typeReference: KSTypeReference, data: String) {
            if (checkVisited(typeReference)) return
            emit("visitTypeReference ===============================================", "$data  ")
            typeReference.annotations.forEach { it.accept(this, "visitTypeReference ") }
            val type = typeReference.resolve()
            type.let {
                emit("resolved to: ${it.declaration.qualifiedName?.asString()}", "visitTypeReference")
            }
            try {
                typeReference.element?.accept(this, "visitTypeReference  ")
            } catch (e: IllegalStateException) {
                emit("TestProcessor: exception: $e", "visitTypeReference")
            }
            emit("visitTypeReference ---------------------------------------------------", "$data  ")
        }

        override fun visitAnnotated(annotated: KSAnnotated, data: String) {
        }

        override fun visitDeclaration(declaration: KSDeclaration, data: String) {
        }

        override fun visitDeclarationContainer(declarationContainer: KSDeclarationContainer, data: String) {
        }

        override fun visitParenthesizedReference(reference: KSParenthesizedReference, data: String) {
        }

        override fun visitClassifierReference(reference: KSClassifierReference, data: String) {
            if (checkVisited(reference)) return
            if (reference.typeArguments.isNotEmpty()) {
                reference.typeArguments.forEach { it.accept(this, "$data  ") }
            }
        }

        override fun visitTypeAlias(typeAlias: KSTypeAlias, data: String) {
        }

        override fun visitValueArgument(valueArgument: KSValueArgument, data: String) {
            if (checkVisited(valueArgument)) return
            emit("visitValueArgument ===============================================", "$data  ")
            val name = valueArgument.name?.asString() ?: "<no name>"
            emit("$name: ${valueArgument.value}", "visitValueArgument")
            valueArgument.annotations.forEach { it.accept(this, "visitValueArgument  ") }
            emit("visitValueArgument -----------------------------------------------", "$data  ")
        }
    }

}

class TestProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return TestProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}
