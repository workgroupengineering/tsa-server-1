package dev.mieser.tsa.web.formatter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigInteger
import java.util.Locale
import java.util.stream.Stream

internal class HexFormatterTest {

    private val testSubject = HexFormatter()

    @Test
    fun parseReturnsNullWhenTextIsBlank() {
        // given / when
        val value = testSubject.parse("\t", Locale.GERMAN)

        // then
        assertThat(value).isNull()
    }

    @ParameterizedTest
    @MethodSource("longToHex")
    fun parseReturnsExpectedValue(longValue: Long, hexValue: String) {
        // given / when
        val value = testSubject.parse(hexValue, Locale.CANADA)

        // then
        assertThat(value).isEqualTo(longValue)
    }

    @ParameterizedTest
    @MethodSource("longToHex")
    fun printReturnsExpectedString(longValue: Long, hexValue: String) {
        // given / when
        val printedValue = testSubject.print(BigInteger.valueOf(longValue), Locale.ENGLISH)

        // then
        assertThat(printedValue).isEqualTo(hexValue)
    }

    companion object {

        @JvmStatic
        fun longToHex(): Stream<Arguments> {
            return Stream.of(
                    Arguments.arguments(-1337L, "-539"),
                    Arguments.arguments(12, "C"),
                    Arguments.arguments(1515, "5EB"))
        }

    }

}
