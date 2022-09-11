package dev.mieser.tsa.web.filter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
internal class TimeStampProtocolFilterTest {

    private val testSubject = TimeStampProtocolFilter(TSP_HANDLER_PORT)

    @Test
    fun callsFilterChainWhenRequestIsNotSentToTspHandlerPort(@Mock requestMock: ServletRequest,
                                                             @Mock responseMock: ServletResponse, @Mock filterChainMock: FilterChain) {
        // given
        given(requestMock.serverPort).willReturn(TSP_HANDLER_PORT + 1)

        // when
        testSubject.doFilter(requestMock, responseMock, filterChainMock)

        // then
        then(filterChainMock).should().doFilter(requestMock, responseMock)
        then(responseMock).shouldHaveNoInteractions()
    }

    @ParameterizedTest
    @ValueSource(strings = ["application/timestamp-query", "application/timestamp-query; charset=UTF-8"])
    fun callsFilterChainWhenPostRequestIsSentToTspHandlerPortAndIsTspRequest(contentType: String,
                                                                             @Mock requestMock: HttpServletRequest, @Mock responseMock: ServletResponse,
                                                                             @Mock filterChainMock: FilterChain) {
        // given
        given(requestMock.serverPort).willReturn(TSP_HANDLER_PORT)
        given(requestMock.contentType).willReturn(contentType)
        given(requestMock.method).willReturn("POST")

        // when
        testSubject.doFilter(requestMock, responseMock, filterChainMock)

        // then
        then(filterChainMock).should().doFilter(requestMock, responseMock)
        then(responseMock).shouldHaveNoInteractions()
    }

    @Test
    fun setsStatusCodeToUnsupportedMediaTypeWhenPostRequestHasBlankContentType(@Mock requestMock: HttpServletRequest,
                                                                               @Mock responseMock: HttpServletResponse, @Mock filterChainMock: FilterChain) {
        // given
        given(requestMock.serverPort).willReturn(TSP_HANDLER_PORT)
        given(requestMock.method).willReturn("POST")
        given(requestMock.contentType).willReturn("\t")

        // when
        testSubject.doFilter(requestMock, responseMock, filterChainMock)

        // then
        then(filterChainMock).shouldHaveNoInteractions()
        then(responseMock).should().status = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE
    }

    @Test
    fun setsStatusCodeToUnsupportedMediaTypeWhenPostRequestHasWrongContentType(@Mock requestMock: HttpServletRequest,
                                                                               @Mock responseMock: HttpServletResponse, @Mock filterChainMock: FilterChain) {
        // given
        given(requestMock.serverPort).willReturn(TSP_HANDLER_PORT)
        given(requestMock.method).willReturn("POST")
        given(requestMock.contentType).willReturn("text/html")

        // when
        testSubject.doFilter(requestMock, responseMock, filterChainMock)

        // then
        then(filterChainMock).shouldHaveNoInteractions()
        then(responseMock).should().status = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE
    }

    @ParameterizedTest
    @ValueSource(strings = ["GET", "PUT", "DELETE"])
    fun setsStatusCodeWhenRequestIsNotAPostRequest(requestMethod: String, @Mock requestMock: HttpServletRequest,
                                                   @Mock responseMock: HttpServletResponse, @Mock filterChainMock: FilterChain) {
        // given
        given(requestMock.serverPort).willReturn(TSP_HANDLER_PORT)
        given(requestMock.method).willReturn(requestMethod)

        // when
        testSubject.doFilter(requestMock, responseMock, filterChainMock)

        // then
        then(filterChainMock).shouldHaveNoInteractions()
        then(responseMock).should().status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
    }

    companion object {

        private const val TSP_HANDLER_PORT = 1337

    }

}
