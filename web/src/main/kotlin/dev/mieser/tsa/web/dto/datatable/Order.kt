package dev.mieser.tsa.web.dto.datatable

/**
 * DTO encapsulating sorting information which is part of a [Datatables](https://datatables.net) AJAX
 * request.
 *
 * @param column The index of the column in the `columns` list.
 * @param dir The sort direction.
 *
 * @see DatatablesPagingRequest
 */
data class Order(val column: Int = 0, val dir: SortDirection = SortDirection.ASC)
