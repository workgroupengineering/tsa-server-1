package dev.mieser.tsa.datetime.api

import java.time.ZonedDateTime
import java.util.Date

/**
 * Converts between legacy [Dates][Date] and the Java Time API introduced in Java 8.
 */
interface DateConverter {
    /**
     * @param date
     *          The date to convert.
     * @return The [ZonedDateTime] representation of the specified date.
     */
    fun toZonedDateTime(date: Date): ZonedDateTime
}
