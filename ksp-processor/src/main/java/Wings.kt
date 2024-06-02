import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.symbol.KSPropertySetter
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ksp.toTypeName


fun Collection<*>.toStr(): String {
    return toTypedArray().contentToString()
}

fun KSAnnotation.toStr(): String {
    return "${shortName.asString()}(${arguments.joinToString { "${it.name?.asString()}=${it.value}" }})"
}

//泛型
fun KSTypeParameter.toStr(): String {
    var fanxingStr = ""
    if (isReified) {
        fanxingStr = "reified "
    }
    fanxingStr += name.getShortName()
    if (bounds.toList().isNotEmpty()) {
        fanxingStr += " : ${bounds.first().toTypeName()}"
    }
    return fanxingStr
}

fun KSValueParameter.toStr(): String {
    var paramStr = ""
    if (isNoInline) {
        paramStr = "noinline "
    } else if (isCrossInline) {
        paramStr = "crossinline "
    }
    if (isVal) {
        paramStr += "val "
    } else if (isVar) {
        paramStr += "var "
    } else if (isVararg) {
        paramStr += "vararg "
    }
    if (hasDefault) {
        return paramStr + "${name?.asString()} : ${type.toTypeName()}[hasDefault]"
    }
    return paramStr + "${name?.asString()} : ${type.toTypeName()}"
}

fun KSClassDeclaration.toStr(): String {
    var classStr = ""
    if (isAbstract()) {
        classStr = "abstract "
    }
    if (isInternal()) {
        classStr = "internal "
    } else if (isPrivate()) {
        classStr = "private "
    } else if (isPublic()) {
        classStr = "public "
    } else if (isOpen()) {
        classStr = "open "
    }
    if (isCompanionObject) {
        classStr += "Companion Object "
    } else {
        classStr += "${qualifiedName?.asString()}"
    }
    if (primaryConstructor != null) {
        classStr += "(${primaryConstructor!!.parameters.joinToString { it.toStr() }})"
    } else {
        classStr += "()"
    }
    if (annotations.toList().isNotEmpty()) {
        return classStr + " =>Anno: ${annotations.joinToString { it.toStr() }}"
    }
    return classStr
}

fun KSPropertyDeclaration.toStr(): String {
    if (annotations.toList().isNotEmpty()) {
        return "${qualifiedName?.asString()} : ${type.toTypeName()} =>Anno: ${annotations.joinToString { it.toStr() }}"
    }
    return "${qualifiedName?.asString()} : ${type.toTypeName()}"
}

fun KSFunctionDeclaration.toStr(): String {
    var funStr = ""
    if (isAbstract) {
        funStr = "abstract "
    }

    funStr += "$functionKind "

    if (isInternal()) {
        funStr += "internal "
    } else if (isPrivate()) {
        funStr += "private "
    } else if (isPublic()) {
        funStr += "public "
    } else if (isOpen()) {
        funStr += "open "
    }
    if (typeParameters.isNotEmpty()) {
        funStr += "<${typeParameters.joinToString { it.toStr() }}>"
    }

    funStr += "${qualifiedName?.asString()}(${parameters.joinToString { it.toStr() }}): ${returnType?.toTypeName()}"
    if (annotations.toList().isNotEmpty()) {
        return funStr + " =>Anno: ${annotations.joinToString { it.toStr() }}"
    }
    return funStr
}

fun KSPropertyGetter.toStr(): String {
    var funStr = receiver.qualifiedName?.asString() ?: ""
    funStr += " get() return ${returnType?.toTypeName()}"

    if (annotations.toList().isNotEmpty()) {
        return funStr + " =>Anno: ${annotations.joinToString { it.toStr() }}"
    }
    return funStr
}

fun KSPropertySetter.toStr(): String {
    var funStr = receiver.qualifiedName?.asString() ?: ""
    funStr += " set(${parameter.toStr()})"

    if (annotations.toList().isNotEmpty()) {
        return funStr + " =>Anno: ${annotations.joinToString { it.toStr() }}"
    }
    return funStr
}
