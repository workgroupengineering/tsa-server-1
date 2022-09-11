package dev.mieser.tsa.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl

@WebMvcTest(IndexController::class)
internal class IndexControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    fun redirectsToHistoryPage() {
        // given / when / then
        mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/web/history"))
    }

}
