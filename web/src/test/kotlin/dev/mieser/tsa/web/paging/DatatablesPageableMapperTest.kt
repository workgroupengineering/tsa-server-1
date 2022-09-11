package dev.mieser.tsa.web.paging

import dev.mieser.tsa.web.dto.datatable.Column
import dev.mieser.tsa.web.dto.datatable.DatatablesPagingRequest
import dev.mieser.tsa.web.dto.datatable.Order
import dev.mieser.tsa.web.dto.datatable.SortDirection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort

internal class DatatablesPageableMapperTest {

    private val testSubject = DatatablesPageableMapper()

    @Test
    fun applyReturnsUnsortedPageableWhenNoOrderSpecified() {
        // given
        val request = DatatablesPagingRequest(0, 25, 50, emptyList(), listOf(Column("name", "data")))

        // when
        val mappedPageable = testSubject.apply(request)

        // then
        assertThat(mappedPageable).isEqualTo(OffsetBasedPageable(25, 50, Sort.unsorted()))
    }

    @Test
    fun applyReturnsPageableWithExpectedSorting() {
        // given
        val request = DatatablesPagingRequest(0, 10, 25, listOf(Order(0, SortDirection.ASC), Order(1, SortDirection.DESC)), listOf(Column("first-name", "first-data"), Column("second-name", "second-data")))

        // when
        val mappedPageable = testSubject.apply(request)

        // then
        val expectedSort = Sort.by(listOf(Sort.Order.asc("first-data"), Sort.Order.desc("second-data")))
        assertThat(mappedPageable).isEqualTo(OffsetBasedPageable(10, 25, expectedSort))
    }

}
