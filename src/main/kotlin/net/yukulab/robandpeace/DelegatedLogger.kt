package net.yukulab.robandpeace

import kotlin.reflect.KProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Example:
 * ```kotlin
 * val logger by DelegatedLogger()
 * ```
 */
class DelegatedLogger {
    private var logger: Logger? = null
    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger =
        logger ?: run {
            val className = thisRef::class.java.name.removeSuffix("\$Companion")
            LoggerFactory.getLogger(className).also { logger = it }
        }
}
