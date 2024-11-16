package test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.yukulab.robandpeace.DelegatedLogger

class TestDelegatedLogger : FunSpec() {
    init {
        test("get class name correctly") {
            logger.name shouldBe "Rob&Peace:test.TestDelegatedLogger"
        }
    }

    companion object {
        private val logger by DelegatedLogger()
    }
}
