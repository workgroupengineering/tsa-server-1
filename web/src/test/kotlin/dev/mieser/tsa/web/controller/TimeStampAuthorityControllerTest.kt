package dev.mieser.tsa.web.controller

import dev.mieser.tsa.domain.TimeStampResponseData
import dev.mieser.tsa.integration.api.IssueTimeStampService
import dev.mieser.tsa.signing.api.exception.InvalidTspRequestException
import dev.mieser.tsa.signing.api.exception.TspResponseException
import dev.mieser.tsa.signing.api.exception.UnknownHashAlgorithmException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.stream.Stream

@WebMvcTest(TimeStampAuthorityController::class)
internal class TimeStampAuthorityControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var issueTimeStampServiceMock: IssueTimeStampService

    @Test
    fun doesNotAcceptOtherContentTypes() {
        // given / when / then
        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType)
    }

    @Test
    fun setsExpectedResponseContentType() {
        // given
        val requestContent = "TSP request".toByteArray(StandardCharsets.UTF_8)
        val responseContent = "TSP response".toByteArray(StandardCharsets.UTF_8)
        val issuedResponse = TimeStampResponseData.builder()
                .asnEncoded(responseContent)
                .build()

        given(issueTimeStampServiceMock.signTimestampRequest(any())).willReturn(issuedResponse)

        // when / then
        mockMvc.perform(post("/")
                .content(requestContent)
                .contentType(REQUEST_CONTENT_TYPE)
                .accept(REPLY_CONTENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(header().stringValues(HttpHeaders.CONTENT_TYPE, REPLY_CONTENT_TYPE))
    }

    @Test
    fun returnsAsnEncodedTspResponseInBody() {
        // given
        val requestContent = "TSP request".toByteArray(StandardCharsets.UTF_8)
        val responseContent = "TSP response".toByteArray(StandardCharsets.UTF_8)
        val issuedResponse = TimeStampResponseData.builder()
                .asnEncoded(responseContent)
                .build()

        given(issueTimeStampServiceMock.signTimestampRequest(any())).willReturn(issuedResponse)

        // when / then
        mockMvc.perform(post("/")
                .content(requestContent)
                .contentType(REQUEST_CONTENT_TYPE)
                .accept(REPLY_CONTENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().bytes(responseContent))

        val inputStreamCaptor = ArgumentCaptor.forClass(InputStream::class.java)
        then(issueTimeStampServiceMock).should().signTimestampRequest(inputStreamCaptor.capture())

        assertThat(inputStreamCaptor.value).hasBinaryContent(requestContent)
    }

    @ParameterizedTest
    @MethodSource("exceptionToStatusCodeProvider")
    fun mapsExceptionToExpectedStatusCode(exception: RuntimeException, expectedStatus: HttpStatus) {
        // given
        val requestContent = "TSP request".toByteArray(StandardCharsets.UTF_8)

        given(issueTimeStampServiceMock.signTimestampRequest(any())).willThrow(exception)

        // when / then
        mockMvc.perform(post("/")
                .content(requestContent)
                .contentType(REQUEST_CONTENT_TYPE)
                .accept(REPLY_CONTENT_TYPE))
                .andExpect(status().`is`(expectedStatus.value()))
    }

    companion object {

        private const val REQUEST_CONTENT_TYPE = "application/timestamp-query"

        private const val REPLY_CONTENT_TYPE = "application/timestamp-reply"

        @JvmStatic
        fun exceptionToStatusCodeProvider(): Stream<Arguments> {
            return Stream.of(
                    arguments(InvalidTspRequestException("Test", IllegalStateException()), HttpStatus.BAD_REQUEST),
                    arguments(UnknownHashAlgorithmException("Test"), HttpStatus.BAD_REQUEST),
                    arguments(TspResponseException("Test", IllegalStateException()), HttpStatus.INTERNAL_SERVER_ERROR))
        }

    }

}
