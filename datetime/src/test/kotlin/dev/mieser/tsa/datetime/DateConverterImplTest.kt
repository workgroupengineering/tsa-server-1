package dev.mieser.tsa.datetime

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

internal class DateConverterImplTest {
    private val testSubject = DateConverterImpl(ZoneId.of("UTC"))

    @Test
    internal fun toZonedDateTimeReturnsExpectedZoneDateTime() {
        // given
        val zonedDateTime = ZonedDateTime.parse("2021-11-13T17:43:30Z")
        val date = Date.from(zonedDateTime.toInstant())

        // when
        val convertedZonedDateTime: ZonedDateTime = testSubject.toZonedDateTime(date)

        // then
        Assertions.assertThat(convertedZonedDateTime).isEqualTo(zonedDateTime)
    }
}
