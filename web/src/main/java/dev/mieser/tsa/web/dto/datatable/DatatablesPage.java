package dev.mieser.tsa.web.dto.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://datatables.net">Datatables</a> paging response.
 *
 * @param <T> The type of the data.
 * @see DatatablesPagingRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatatablesPage<T> {

    /**
     * The draw counter included in the paging request.
     */
    private int draw;

    /**
     * The total number of records in the database.
     */
    private long recordsTotal;

    /**
     * The number of filtered records. Always set to zero, since no searching is supported.
     */
    private long recordFiltered;

    /**
     * The page entries.
     */
    private List<T> data;

}
