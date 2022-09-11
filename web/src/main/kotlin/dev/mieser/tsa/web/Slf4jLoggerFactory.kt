package dev.mieser.tsa.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

class Slf4jLoggerFactory {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger = LoggerFactory.getLogger(thisRef!!.javaClass)

}
