package dev.mieser.tsa.datetime

import dev.mieser.tsa.datetime.api.CurrentDateService
import java.time.Clock
import java.time.ZonedDateTime
import java.util.Date

class CurrentDateServiceImpl(private val clock: Clock) : CurrentDateService {
    override fun now(): Date {
        val now = ZonedDateTime.now(clock)
        return Date.from(now.toInstant())
    }
}
