package dev.mieser.tsa.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mieser.tsa.domain.TimeStampResponseData
import dev.mieser.tsa.integration.api.QueryTimeStampResponseService
import dev.mieser.tsa.web.dto.datatable.Column
import dev.mieser.tsa.web.dto.datatable.DatatablesPagingRequest
import dev.mieser.tsa.web.paging.DatatablesPageableMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigInteger

@WebMvcTest(HistoryRestController::class)
internal class HistoryRestControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var queryTimeStampResponseServiceMock: QueryTimeStampResponseService

    @MockBean
    private lateinit var datatablesPageableMapperMock: DatatablesPageableMapper

    @Test
    fun returnsExpectedPageOfResponses() {
        // given
        val objectMapper = ObjectMapper()
        val timeStampResponse = TimeStampResponseData.builder()
                .serialNumber(BigInteger.valueOf(8L))
                .build()
        val pagingRequest = DatatablesPagingRequest(12, 0, 1, emptyList(), listOf(Column("name", "data")))
        val mappedPageable = PageRequest.of(0, 1)
        val page = PageImpl(listOf(timeStampResponse), mappedPageable, 1337)

        given(datatablesPageableMapperMock.apply(pagingRequest)).willReturn(mappedPageable)
        given(queryTimeStampResponseServiceMock.findAll(mappedPageable)).willReturn(page)

        // when / then
        mockMvc.perform(post("/api/history")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagingRequest)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.draw").value("12"))
                .andExpect(jsonPath("$.recordsTotal").value("1337"))
                .andExpect(jsonPath("$.recordsFiltered").value("1337"))
                .andExpect(jsonPath("$.data.length()").value("1"))
                .andExpect(jsonPath("$.data[0].serialNumber").value("8"))
    }

}
