package dev.mieser.tsa.datetime

import dev.mieser.tsa.datetime.api.DateConverter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class DateConverterImpl(private val zoneId: ZoneId) : DateConverter {
    override fun toZonedDateTime(date: Date): ZonedDateTime = date.toInstant().atZone(zoneId)
}
