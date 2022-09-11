package dev.mieser.tsa.web.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import javax.validation.ConstraintValidatorContext

@ExtendWith(MockitoExtension::class)
internal class TcpPortValidatorTest {

    private val testSubject = TcpPortValidator()

    @Test
    fun isValidReturnsTrueWhenValueIsNull(@Mock contextMock: ConstraintValidatorContext) {
        // given // when
        val valid = testSubject.isValid(null, contextMock)

        // then
        assertThat(valid).isTrue
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 1337, 10_133, 65_535])
    fun isValidReturnsTrueWhenValueIsValidTcpPort(port: Int, @Mock contextMock: ConstraintValidatorContext) {
        // given // when
        val valid = testSubject.isValid(port, contextMock)

        // then
        assertThat(valid).isTrue
    }

    @ParameterizedTest
    @ValueSource(ints = [-23, 0, 65_536, Int.MAX_VALUE])
    fun isValidReturnsFalseWhenValueIsNotAValidTcpPort(port: Int, @Mock contextMock: ConstraintValidatorContext) {
        // given // when
        val valid = testSubject.isValid(port, contextMock)

        // then
        assertThat(valid).isFalse
    }

}
