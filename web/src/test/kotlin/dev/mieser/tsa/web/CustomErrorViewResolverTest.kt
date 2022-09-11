package dev.mieser.tsa.web

import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletRequest

@ExtendWith(MockitoExtension::class)
internal class CustomErrorViewResolverTest {

    private val testSubject = CustomErrorViewResolver()

    @Test
    fun resolveErrorViewThrowsExceptionWhenStatusNotPresent(@Mock servletRequestMock: HttpServletRequest) {
        // given
        val emptyModel = emptyMap<String, Any>()

        // when / then
        assertThatIllegalArgumentException()
                .isThrownBy { testSubject.resolveErrorView(servletRequestMock, HttpStatus.FORBIDDEN, emptyModel) }
                .withMessage("HTTP Status Code not present in model.")
    }

    @Test
    fun resolveErrorViewReturnsExpectedModelAndView(@Mock servletRequestMock: HttpServletRequest) {
        // given
        val model = mapOf<String, Any>("status" to 200)

        // when
        val actualModelAndView = testSubject.resolveErrorView(servletRequestMock, HttpStatus.BAD_REQUEST, model)

        // then
        assertSoftly { softly ->
            softly.assertThat(actualModelAndView.model).isEqualTo(model)
            softly.assertThat(actualModelAndView.viewName).isEqualTo("error")
        }
    }

}
