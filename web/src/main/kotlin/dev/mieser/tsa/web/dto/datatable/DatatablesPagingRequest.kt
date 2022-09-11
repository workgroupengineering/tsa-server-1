package dev.mieser.tsa.web.dto.datatable

/**
 * DTO encapsulating part of the request parameters sent by [Datatables](https://datatables.net) AJAX
 * requests. Searching is not supported.
 *
 * @param draw A draw counter used by Datatables. The same value must be included in the response.
 * @param start The zero-based index of the first item to include.
 * @param length The number of items to include.
 * @param order The table columns to sort by.
 * @param columns The columns of the table.
 *
 * @see DatatablesPage
 */
data class DatatablesPagingRequest(val draw: Int = 0, val start: Int = 0, val length: Int = 0, val order: List<Order> = emptyList(), val columns: List<Column> = emptyList())
