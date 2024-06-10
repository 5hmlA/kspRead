package process

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

context(SymbolProcessorEnvironment)
fun ClassDeclarationByName(resolver: Resolver) {
    //根据类名字找到对应的的ClassDeclaration
    //根据注解名字找到对应的的ClassDeclaration
    val classDeclaration = resolver.getClassDeclarationByName(resolver.getKSNameFromString("com.example.kspt.Testt"))
    logger.warn(classDeclaration.toString())
}