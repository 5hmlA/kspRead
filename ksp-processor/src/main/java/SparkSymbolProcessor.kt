import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSNode
import process.codeGenerator


fun KSPLogger.log(msg: String, node: KSNode? = null) {
    warn(msg, null)
//    warn(msg, node)
}

class SparkSymbolProcessor(val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    /**
     * [SymbolProcessor] is the interface used by plugins to integrate into Kotlin Symbol Processing.
     * SymbolProcessor supports multiple rounds of execution, a processor may return a list of deferred symbols at the end
     * of every round, which will be passed to processors again in the next round, together with the newly generated symbols.
     * On exceptions, KSP will try to distinguish between exceptions from KSP and exceptions from processors.
     * Exceptions from processors will immediately terminate processing and be logged as an error in KSPLogger.
     * Exceptions from KSP should be reported to KSP developers for further investigation.
     * At the end of the round where exceptions or errors happened, all processors will invoke onError() function to do
     * their own error handling.
     *
     * @return 表示处理过程中剩余的需要重新处理的符号。这个返回值的主要作用是告知编译器哪些符号在当前轮次中未能完全处理，需要在后续轮次中再次处理。
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        environment.apply {
            logger.log("> > > > > > > $this@SparkSymbolProcessor ...")
            options.entries.forEach {
                logger.log("> options: ${it.key} = ${it.value}")
            }
//            traverseAllFiles(resolver)
//            symbolsWithAnnotation(resolver)
            codeGenerator(resolver)
        }
        return emptyList()
    }

    override fun onError() {
        super.onError()
    }

    override fun finish() {
        super.finish()
    }
}


class SparkSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SparkSymbolProcessor(environment)
    }
}