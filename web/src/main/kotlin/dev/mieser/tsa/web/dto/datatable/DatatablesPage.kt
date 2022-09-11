package dev.mieser.tsa.web.dto.datatable

/**
 * [Datatables](https://datatables.net) paging response.
 *
 * @param T
 * The type of the data.
 * @param draw  The draw counter included in the paging request.
 * @param recordsTotal The total number of records in the database.
 * @param recordsFiltered The number of records after filtering.
 * @param data The page entries.
 * @see DatatablesPagingRequest
 */
data class DatatablesPage<T>(val draw: Int = 0, val recordsTotal: Long = 0, val recordsFiltered: Long = 0, val data: List<T> = emptyList())
