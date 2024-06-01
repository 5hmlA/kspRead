import com.google.wireless.android.sdk.stats.GradleBuildVariant.KotlinOptions

plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    // Or shorter:
    jvmToolchain(18)
}

//https://kotlinlang.org/docs/ksp-incremental.html#aggregating-vs-isolating
dependencies {
    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
    implementation(libs.ksp.process.api)
}

//KSFile
//  packageName: KSName
//  fileName: String
//  annotations: List<KSAnnotation>  (File annotations)
//  declarations: List<KSDeclaration>
//    KSClassDeclaration // class, interface, object
//      simpleName: KSName
//      qualifiedName: KSName
//      containingFile: String
//      typeParameters: KSTypeParameter
//      parentDeclaration: KSDeclaration
//      classKind: ClassKind
//      primaryConstructor: KSFunctionDeclaration
//      superTypes: List<KSTypeReference>
//      // contains inner classes, member functions, properties, etc.
//      declarations: List<KSDeclaration>
//    KSFunctionDeclaration // top level function
//      simpleName: KSName
//      qualifiedName: KSName
//      containingFile: String
//      typeParameters: KSTypeParameter
//      parentDeclaration: KSDeclaration
//      functionKind: FunctionKind
//      extensionReceiver: KSTypeReference?
//      returnType: KSTypeReference
//      parameters: List<KSValueParameter>
//      // contains local classes, local functions, local variables, etc.
//      declarations: List<KSDeclaration>
//    KSPropertyDeclaration // global variable
//      simpleName: KSName
//      qualifiedName: KSName
//      containingFile: String
//      typeParameters: KSTypeParameter
//      parentDeclaration: KSDeclaration
//      extensionReceiver: KSTypeReference?
//      type: KSTypeReference
//      getter: KSPropertyGetter
//        returnType: KSTypeReference
//      setter: KSPropertySetter
//        parameter: KSValueParameter