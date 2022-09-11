package dev.mieser.tsa.web.paging

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.data.domain.Sort
import java.util.stream.Stream

internal class OffsetBasedPageableTest {

    @ParameterizedTest(name = "{displayName} (offset {0,number,#})")
    @ValueSource(longs = [-1337L, -10L, -1L])
    fun constructorThrowsExceptionWhenOffsetIsInvalid(illegalOffset: Long) {
        // given / when / then
        assertThatIllegalArgumentException()
                .isThrownBy { OffsetBasedPageable(illegalOffset, 15, Sort.unsorted()) }
                .withMessage("Offset cannot be less than zero.")
    }

    @ParameterizedTest(name = "{displayName} (pageSize {0,number,#})")
    @ValueSource(ints = [-1337, -1, 0])
    fun constructorThrowsExceptionWhenPageSizeIsInvalid(illegalPageSize: Int) {
        // given / when / then
        assertThatIllegalArgumentException()
                .isThrownBy { OffsetBasedPageable(15L, illegalPageSize, Sort.unsorted()) }
                .withMessage("Page size must be greater than or equal to one.")
    }

    @MethodSource("offsetAndPageSizeToPageNumber")
    @ParameterizedTest(name = "{displayName} (offset {0,number,#}, pageSize {1,number,#}, expectedPageNumber {2,number,#}")
    fun getPageNumberReturnsExpectedResult(offset: Long, pageSize: Int, expectedPageNumber: Int) {
        // given
        val testSubject = OffsetBasedPageable(offset, pageSize, Sort.unsorted())

        // when
        val actualPageNumber = testSubject.pageNumber

        // then
        assertThat(actualPageNumber).isEqualTo(expectedPageNumber)
    }

    @Test
    fun pageSizeReturnsPageSize() {
        // given
        val pageSize = 1337

        // when
        val testSubject = OffsetBasedPageable(10L, pageSize, Sort.unsorted())

        // then
        assertThat(testSubject.pageSize).isEqualTo(pageSize)
    }

    @Test
    fun offsetReturnsOffset() {
        // given
        val offset = 1337L

        // when
        val testSubject = OffsetBasedPageable(offset, 31, Sort.unsorted())

        // then
        assertThat(testSubject.offset).isEqualTo(offset)
    }

    @Test
    fun sortReturnsExpectedSort() {
        // given
        val sort = Sort.by("test1", "test2")

        // when
        val testSubject = OffsetBasedPageable(12L, 31, sort)

        // then
        assertThat(testSubject.sort).isEqualTo(sort)
    }

    @Test
    fun nextReturnsExpectedPage() {
        // given
        val testSubject = OffsetBasedPageable(12L, 67, Sort.unsorted())

        // when
        val next = testSubject.next()

        // then
        assertThat(next).isEqualTo(OffsetBasedPageable(79, 67, Sort.unsorted()))
    }

    @Test
    fun previousOrFirstReturnsFirstPageWhenNoPreviousExists() {
        // given
        val testSubject = OffsetBasedPageable(15L, 30, Sort.unsorted())

        // when
        val previousOrFirst = testSubject.previousOrFirst()

        // then
        assertThat(previousOrFirst).isEqualTo(OffsetBasedPageable(0, 30, Sort.unsorted()))
    }

    @Test
    fun previousOrFirstReturnsPreviousPageWhenPreviousPageExists() {
        // given
        val testSubject = OffsetBasedPageable(1500L, 30, Sort.unsorted())

        // when
        val previousOrFirst = testSubject.previousOrFirst()

        // then
        assertThat(previousOrFirst).isEqualTo(OffsetBasedPageable(1470, 30, Sort.unsorted()))
    }

    @Test
    fun firstReturnsExpectedPage() {
        // given
        val testSubject = OffsetBasedPageable(1337L, 30, Sort.unsorted())

        // when
        val first = testSubject.first()

        // then
        assertThat(first).isEqualTo(OffsetBasedPageable(0, 30, Sort.unsorted()))
    }

    @Test
    fun withPageReturnsExpectedPage() {
        // given
        val testSubject = OffsetBasedPageable(1337L, 27, Sort.unsorted())

        // when
        val withPage = testSubject.withPage(12)

        // then
        assertThat(withPage).isEqualTo(OffsetBasedPageable(324L, 27, Sort.unsorted()))
    }

    @ParameterizedTest
    @MethodSource("offsetAndPageSizeToPrevious")
    fun hasPreviousReturnsExpectedResult(offset: Long, pageSize: Int, shouldHavePrevious: Boolean) {
        // given
        val testSubject = OffsetBasedPageable(offset, pageSize, Sort.unsorted())

        // when
        val hasPrevious = testSubject.hasPrevious()

        // then
        assertThat(hasPrevious).isEqualTo(shouldHavePrevious)
    }

    companion object {

        @JvmStatic
        fun offsetAndPageSizeToPageNumber(): Stream<Arguments> {
            return Stream.of(
                    arguments(0L, 1, 0),
                    arguments(1L, 1, 1),
                    arguments(10L, 5, 2),
                    arguments(10L, 7, 1))
        }

        @JvmStatic
        fun offsetAndPageSizeToPrevious(): Stream<Arguments> {
            return Stream.of(
                    arguments(10L, 7, true),
                    arguments(7L, 7, true),
                    arguments(3L, 7, false))
        }

    }

}
