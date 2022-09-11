package dev.mieser.tsa.web.controller

import dev.mieser.tsa.domain.TimeStampValidationResult
import dev.mieser.tsa.integration.api.ValidateTimeStampResponseService
import dev.mieser.tsa.signing.api.exception.InvalidTspResponseException
import dev.mieser.tsa.web.ApplicationVersionService
import dev.mieser.tsa.web.dto.TimeStampResponseDto
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.Locale

@WebMvcTest(ValidateController::class)
@Import(ApplicationVersionService::class)
internal class ValidateControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var validateTimeStampResponseServiceMock: ValidateTimeStampResponseService

    @Test
    fun canRenderValidationPageWithEmptyModel() {
        // given / when / then
        mockMvc.perform(get("/web/validate"))
                .andExpect(status().isOk)
                .andExpect(view().name("validate"))
                .andExpect(model().attribute("response", TimeStampResponseDto()))
    }

    @Test
    fun rendersValidationResultPageWhenValidResponseIsEntered() {
        // given
        val base64EncodedTspResponse = "dGVzdA=="
        val validationResult = TimeStampValidationResult.builder().build()

        given(validateTimeStampResponseServiceMock.validateTimeStampResponse(base64EncodedTspResponse)).willReturn(validationResult)

        // when / then
        mockMvc.perform(post("/web/validate")
                .locale(Locale.ENGLISH)
                .accept(MediaType.TEXT_HTML)
                .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr("response", TimeStampResponseDto(base64EncodedTspResponse)))
                .andExpect(status().isOk)
                .andExpect(view().name("validation-result"))
                .andExpect(model().attribute("validationResult", validationResult))
    }

    @Test
    fun showsErrorWhenInputIsNotBase64Encoded() {
        // given
        val illegalInput = TimeStampResponseDto("ü")

        // when / then
        val renderedHtml = mockMvc.perform(post("/web/validate")
                .locale(Locale.ENGLISH)
                .accept(MediaType.TEXT_HTML)
                .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr("response", illegalInput))
                .andExpect(status().isOk)
                .andExpect(view().name("validate"))
                .andExpect(model().attribute("response", illegalInput))
                .andReturn().response.contentAsString

        val warningAlertText = Jsoup.parse(renderedHtml).select(".alert-warning").text()
        assertThat(warningAlertText).isEqualTo("Not a valid Base64 String.")
    }

    @Test
    fun showsErrorWhenInvalidTspResponseIsEntered() {
        // given
        val base64EncodedTspResponse = "dGVzdA=="

        given(validateTimeStampResponseServiceMock.validateTimeStampResponse(base64EncodedTspResponse))
                .willThrow(InvalidTspResponseException("Error!1!!"))

        // when / then
        val renderedHtml = mockMvc.perform(post("/web/validate")
                .locale(Locale.ENGLISH)
                .accept(MediaType.TEXT_HTML)
                .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr("response", TimeStampResponseDto(base64EncodedTspResponse)))
                .andExpect(status().isBadRequest)
                .andExpect(view().name("validate"))
                .andExpect(model().attribute("response", TimeStampResponseDto()))
                .andReturn().response.contentAsString

        val dangerAlertText = Jsoup.parse(renderedHtml).select(".alert-danger").text()
        assertThat(dangerAlertText).isEqualTo("The input is not a valid time stamp response according to RFC 3161/RFC 5816.")
    }

}
