package com.example.kspt

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor

class SymboProcessorTest(val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles()
        val symbolsWithAnnotation = resolver.getSymbolsWithAnnotation("com.example.kspt.Testt")
//        resolver.getAllFiles()
//        KSDefaultVisitor
        symbolsWithAnnotation.filter { it.validate() }.forEach {
            it.accept(com.example.kspt.KSVisitor(), Unit)
        }

        return symbolsWithAnnotation.filter { !it.validate() }.toList()
    }
}

class KSVisitor : KSVisitorVoid() {
    override fun visitAnnotated(annotated: KSAnnotated, data: Unit) {
        super.visitAnnotated(annotated, data)
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
        super.visitAnnotation(annotation, data)
    }

    override fun visitCallableReference(reference: KSCallableReference, data: Unit) {
        super.visitCallableReference(reference, data)
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        super.visitClassDeclaration(classDeclaration, data)
    }

    override fun visitClassifierReference(reference: KSClassifierReference, data: Unit) {
        super.visitClassifierReference(reference, data)
    }

    override fun visitDeclaration(declaration: KSDeclaration, data: Unit) {
        super.visitDeclaration(declaration, data)
    }

    override fun visitDeclarationContainer(declarationContainer: KSDeclarationContainer, data: Unit) {
        super.visitDeclarationContainer(declarationContainer, data)
    }

    override fun visitDefNonNullReference(reference: KSDefNonNullReference, data: Unit) {
        super.visitDefNonNullReference(reference, data)
    }

    override fun visitDynamicReference(reference: KSDynamicReference, data: Unit) {
        super.visitDynamicReference(reference, data)
    }

    override fun visitFile(file: KSFile, data: Unit) {
        super.visitFile(file, data)
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        super.visitFunctionDeclaration(function, data)
    }

    override fun visitModifierListOwner(modifierListOwner: KSModifierListOwner, data: Unit) {
        super.visitModifierListOwner(modifierListOwner, data)
    }

    override fun visitNode(node: KSNode, data: Unit) {
        super.visitNode(node, data)
    }

    override fun visitParenthesizedReference(reference: KSParenthesizedReference, data: Unit) {
        super.visitParenthesizedReference(reference, data)
    }

    override fun visitPropertyAccessor(accessor: KSPropertyAccessor, data: Unit) {
        super.visitPropertyAccessor(accessor, data)
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        super.visitPropertyDeclaration(property, data)
    }

    override fun visitPropertyGetter(getter: KSPropertyGetter, data: Unit) {
        super.visitPropertyGetter(getter, data)
    }

    override fun visitPropertySetter(setter: KSPropertySetter, data: Unit) {
        super.visitPropertySetter(setter, data)
    }

    override fun visitReferenceElement(element: KSReferenceElement, data: Unit) {
        super.visitReferenceElement(element, data)
    }

    override fun visitTypeAlias(typeAlias: KSTypeAlias, data: Unit) {
        super.visitTypeAlias(typeAlias, data)
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
        super.visitTypeArgument(typeArgument, data)
    }

    override fun visitTypeParameter(typeParameter: KSTypeParameter, data: Unit) {
        super.visitTypeParameter(typeParameter, data)
    }

    override fun visitTypeReference(typeReference: KSTypeReference, data: Unit) {
        super.visitTypeReference(typeReference, data)
    }

    override fun visitValueArgument(valueArgument: KSValueArgument, data: Unit) {
        super.visitValueArgument(valueArgument, data)
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: Unit) {
        super.visitValueParameter(valueParameter, data)
    }
}

class SymbolProcessorProviderTest : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SymboProcessorTest(environment)
    }
}