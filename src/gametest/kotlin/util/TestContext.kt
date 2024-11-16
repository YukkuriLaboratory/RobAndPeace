package util

import net.minecraft.test.GameTestException
import net.minecraft.test.TestContext
import org.slf4j.Logger

fun TestContext.withAssertion(logger: Logger? = null, assertion: () -> Unit) {
    kotlin.runCatching {
        assertion()
    }.onFailure {
        logger?.error("Assertion failed", it) ?: it.printStackTrace()
        throw GameTestException(it.message)
    }
}

fun TestContext.addInstantFinalTaskWithAssertion(logger: Logger? = null, assertion: () -> Unit) {
    addInstantFinalTask {
        withAssertion(logger, assertion)
    }
}
