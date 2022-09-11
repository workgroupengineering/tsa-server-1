package dev.mieser.tsa.web.paging

import dev.mieser.tsa.web.dto.datatable.Column
import dev.mieser.tsa.web.dto.datatable.DatatablesPagingRequest
import dev.mieser.tsa.web.dto.datatable.Order
import dev.mieser.tsa.web.dto.datatable.SortDirection
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Mapper [Function] to map [DatatablesPagingRequest]s to Spring Data [Pageable]s.
 */
@Component
open class DatatablesPageableMapper : Function<DatatablesPagingRequest, Pageable> {

    override fun apply(pagingRequest: DatatablesPagingRequest): Pageable {
        return OffsetBasedPageable(pagingRequest.start.toLong(), pagingRequest.length, mapSorting(pagingRequest))
    }

    private fun mapSorting(pagingRequest: DatatablesPagingRequest): Sort {
        val orders = pagingRequest.order
        if (orders.isEmpty()) {
            return Sort.unsorted()
        }
        val columns = pagingRequest.columns
        val pageableOrder = orders.map { order -> mapOrder(columns, order) }
        return Sort.by(pageableOrder)
    }

    private fun mapOrder(columns: List<Column>, order: Order): Sort.Order {
        val column = columns[order.column]
        val sortProperty = column.data
        return when (order.dir) {
            SortDirection.ASC -> Sort.Order.asc(sortProperty)
            SortDirection.DESC -> Sort.Order.desc(sortProperty)
        }
    }

}
