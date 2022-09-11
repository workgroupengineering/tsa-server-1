package dev.mieser.tsa.web.paging

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * [Pageable] Implementation which uses a predefined offset.
 *
 * @param offset  The number of items to skip. Must be greater than or equal to zero.
 * @param pageSize The number of items on each page. Must be greater than or equal to one.
 * @param sort The sorting to user. May be `null`.
 */
data class OffsetBasedPageable(private val offset: Long, private val pageSize: Int, private val sort: Sort) : Pageable {

    init {
        require(offset >= 0) { "Offset cannot be less than zero." }
        require(pageSize >= 1) { "Page size must be greater than or equal to one." }
    }

    override fun getPageNumber(): Int {
        return (offset / pageSize).toInt()
    }

    override fun getPageSize(): Int {
        return pageSize
    }

    override fun getOffset(): Long {
        return offset
    }

    override fun getSort(): Sort {
        return sort
    }

    override fun next(): Pageable {
        return OffsetBasedPageable(offset + pageSize, pageSize, sort)
    }

    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) OffsetBasedPageable(offset - pageSize, pageSize, sort) else first()
    }

    override fun first(): Pageable {
        return OffsetBasedPageable(0, pageSize, sort)
    }

    override fun withPage(pageNumber: Int): Pageable {
        return OffsetBasedPageable(pageSize.toLong() * pageNumber, pageSize, sort)
    }

    override fun hasPrevious(): Boolean {
        return offset >= pageSize
    }
}
