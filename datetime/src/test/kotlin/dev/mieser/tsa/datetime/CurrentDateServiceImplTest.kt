package dev.mieser.tsa.datetime

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

internal class CurrentDateServiceImplTest {
    @Test
    internal fun nowReadsTimeFromClock() {
        // given
        val now = ZonedDateTime.parse("2021-11-13T17:46:51Z")
        val fixedClock = Clock.fixed(now.toInstant(), ZoneId.of("UTC"))

        val testSubject = CurrentDateServiceImpl(fixedClock)

        // when
        val nowAsDate = testSubject.now()

        // then
        assertThat(ZonedDateTime.ofInstant(nowAsDate.toInstant(), ZoneId.of("UTC"))).isEqualTo(now)
    }
}
