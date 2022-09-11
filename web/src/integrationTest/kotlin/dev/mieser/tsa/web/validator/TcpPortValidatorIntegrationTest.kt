package dev.mieser.tsa.web.validator

import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale
import javax.validation.Validation

internal class TcpPortValidatorIntegrationTest {

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
        assertValidationMessage(Locale.ENGLISH, "Must be a valid TCP Port between 1 and 65,535 (provided: -1)")
    }

    @Test
    fun hasExpectedGermanValidationMessage() {
        // given / when / then
        assertValidationMessage(Locale.GERMAN, "Muss ein valider TCP Port zwischen 1 und 65.535 sein (konfiguriert: -1)")
    }

    private fun assertValidationMessage(locale: Locale, expectedMessage: String) {
        // given
        Locale.setDefault(locale)
        val validationTarget = ValidationTarget(-1)
        val validator = Validation.buildDefaultValidatorFactory().validator

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

    private data class ValidationTarget(@field:TcpPort private val value: Int)

}
