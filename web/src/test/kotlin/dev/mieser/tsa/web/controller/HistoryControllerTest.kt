package dev.mieser.tsa.web.controller

import dev.mieser.tsa.web.ApplicationVersionService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

/**
 * @implNote Imports the actual [ApplicationVersionService] so the [MockBean] has the same bean name. The
 * bean name is used in the footer template.
 */
@Import(ApplicationVersionService::class)
@WebMvcTest(HistoryController::class)
internal class HistoryControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var applicationVersionServiceMock: ApplicationVersionService

    @Test
    fun rendersExpectedView() {
        // given / when / then
        mockMvc.perform(get("/web/history").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk)
                .andExpect(view().name("history"))
    }

    @Test
    fun footerContainsExpectedVersion() {
        // given
        given(applicationVersionServiceMock.applicationVersion).willReturn("1.2.3")

        // when / then
        val renderedHtml = mockMvc.perform(get("/web/history").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
        val actualVersion = Jsoup.parse(renderedHtml).select("body footer span").text()

        assertThat(actualVersion).isEqualTo("Version 1.2.3")
    }

}
