package dev.mieser.tsa.web.validator

import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale
import javax.validation.Validation

internal class Base64EncodingValidatorIntegrationTest {

    private lateinit var defaultLocale: Locale

    @BeforeEach
    fun setUp() {
        defaultLocale = Locale.getDefault()
    }

    @AfterEach
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun hasExpectedEnglishValidationMessage() {
        // given / when / then
        assertValidationMessage(Locale.ENGLISH, "Not a valid Base64 String.")
    }

    @Test
    fun hasExpectedGermanValidationMessage() {
        // given / when / then
        assertValidationMessage(Locale.GERMAN, "Es handelt sich nicht um einen validen Base64 String.")
    }

    private fun assertValidationMessage(locale: Locale, expectedMessage: String) {
        // given
        Locale.setDefault(locale)
        val value = "I'm Base64 (maybe)!"
        val validationTarget = ValidationTarget(value)

        // when
        val violations = Validation.buildDefaultValidatorFactory().use {
            it.validator.validate(validationTarget)
        }

        // then
        assertSoftly { softly ->
            softly.assertThat(violations).hasSize(1)
            softly.assertThat(violations).first().extracting { it.message }.isEqualTo(expectedMessage)
        }
    }

    private data class ValidationTarget(@field:Base64Encoded private val value: String)

}
