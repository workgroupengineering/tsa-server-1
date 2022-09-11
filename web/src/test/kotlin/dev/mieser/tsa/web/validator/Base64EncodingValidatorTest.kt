package dev.mieser.tsa.web.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import javax.validation.ConstraintValidatorContext

@ExtendWith(MockitoExtension::class)
internal class Base64EncodingValidatorTest {

    private val testSubject = Base64EncodingValidator()

    @Test
    fun isValidReturnsTrueWhenValueIsNull(@Mock contextMock: ConstraintValidatorContext) {
        // given / when
        val valid = testSubject.isValid(null, contextMock)

        // then
        assertThat(valid).isTrue
    }

    @Test
    fun isValidReturnsTrueWhenValueIsBase64(@Mock contextMock: ConstraintValidatorContext) {
        // given
        val value = "dGVzdA=="

        // when
        val valid = testSubject.isValid(value, contextMock)

        // then
        assertThat(valid).isTrue
    }

    @Test
    fun isValidReturnsFalseWhenValueIsNotBase64(@Mock contextMock: ConstraintValidatorContext) {
        // given
        val value = "I'm Base64, trust me, bro"

        // when
        val valid = testSubject.isValid(value, contextMock)

        // then
        assertThat(valid).isFalse
    }

}
