package dev.mieser.tsa.web.formatter

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets
import java.util.Locale

internal class Base64FormatterTest {

    private val testSubject = Base64Formatter()

    @Test
    fun parseThrowsExceptionWhenStringCannotBeDecoded() {
        // given
        val illegalBase64 = "I'm Base64, trust me, bro!"

        // when / then
        assertThatIllegalArgumentException()
                .isThrownBy { testSubject.parse(illegalBase64, Locale.GERMAN) }
                .withMessage("Not a valid Base64 string.")
    }

    @Test
    fun parseReturnsDecodedBinaryData() {
        // given
        val encoded = "NzM1NTYwOA=="

        // when
        val decodedBinaryData = testSubject.parse(encoded, Locale.ENGLISH)

        // then
        assertThat(decodedBinaryData).isEqualTo("7355608".toByteArray(StandardCharsets.UTF_8))
    }

    @Test
    fun printReturnsEncodedBinaryData() {
        // given
        val binaryData = "7355608".toByteArray(StandardCharsets.UTF_8)

        // when
        val encodedData = testSubject.print(binaryData, Locale.CANADA)

        // then
        assertThat(encodedData).isEqualTo("NzM1NTYwOA==")
    }

}
