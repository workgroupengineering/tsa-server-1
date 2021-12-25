package dev.mieser.tsa.web.dto.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DTO encapsulating sorting information which is part of a <a href="https://datatables.net">Datatables</a> AJAX request.
 *
 * @see DatatablesPagingRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * The index of the column in the {@code columns} list.
     */
    @Min(0)
    private int column;

    /**
     * The sort direction.
     */
    @NotNull
    private SortDirection dir;

}