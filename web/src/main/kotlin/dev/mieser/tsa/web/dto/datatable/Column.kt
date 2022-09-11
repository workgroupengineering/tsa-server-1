package dev.mieser.tsa.web.dto.datatable

/**
 * DTO encapsulating parts of the column information included in a [Datatables](https://datatables.net) AJAX
 * request.
 *
 * @param name The column's name.
 * @param data The column's data source.
 *
 * @see DatatablesPagingRequest
 */
data class Column(val name: String? = null, val data: String? = null)
