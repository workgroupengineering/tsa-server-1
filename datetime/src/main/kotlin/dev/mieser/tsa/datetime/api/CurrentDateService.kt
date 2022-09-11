package dev.mieser.tsa.datetime.api

import java.util.Date

/**
 * Interface abstraction of a service returning the current [Date].
 */
interface CurrentDateService {
    /**
     * @return The current [Date].
     */
    fun now(): Date
}
