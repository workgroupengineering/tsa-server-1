package dev.mieser.tsa.web.controller

import dev.mieser.tsa.domain.TimeStampResponseData
import dev.mieser.tsa.integration.api.QueryTimeStampResponseService
import dev.mieser.tsa.web.dto.datatable.DatatablesPage
import dev.mieser.tsa.web.dto.datatable.DatatablesPagingRequest
import dev.mieser.tsa.web.paging.DatatablesPageableMapper
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * REST API Endpoint for querying responses sent by this TSA. The API is compatible with
 * [Datatables](https://datatables.net) AJAX requests.
 *
 * @see HistoryController
 */
@RestController
@RequestMapping(path = ["/api/history"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
class HistoryRestController(private val queryTimeStampResponseService: QueryTimeStampResponseService, private val datatablesPageableMapper: DatatablesPageableMapper) {

    @PostMapping
    fun history(@Valid @RequestBody request: DatatablesPagingRequest): DatatablesPage<TimeStampResponseData> {
        val page = queryTimeStampResponseService.findAll(datatablesPageableMapper.apply(request))
        return DatatablesPage(request.draw, page.totalElements, page.totalElements, page.content)
    }

}
