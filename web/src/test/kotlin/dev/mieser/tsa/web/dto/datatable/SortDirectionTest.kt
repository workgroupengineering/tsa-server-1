package dev.mieser.tsa.web.dto.datatable

import dev.mieser.tsa.web.dto.datatable.SortDirection.Companion.fromDirection
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SortDirectionTest {

    @Test
    fun fromDirectionThrowsExceptionWhenNoEnumConstantIsPresent() {
        // given / when / then
        assertThatIllegalArgumentException()
                .isThrownBy { fromDirection("unknown") }
                .withMessage("No direction constant found for 'unknown'.")
    }

    @ParameterizedTest
    @MethodSource("directionToEnumConstant")
    fun fromDirectionReturnsExpectedConstant(direction: String, expectedSortDirection: SortDirection) {
        // given / when
        val actualSortDirection = fromDirection(direction)

        // then
        assertThat(actualSortDirection).isEqualTo(expectedSortDirection)
    }

    @Test
    fun fromDirectionIsCaseInsensitive() {
        // given / when / then
        assertThat(fromDirection("AsC")).isEqualTo(SortDirection.ASC)
    }

    companion object {

        @JvmStatic
        fun directionToEnumConstant(): Stream<Arguments> {
            return Stream.of(
                    Arguments.arguments("asc", SortDirection.ASC),
                    Arguments.arguments("desc", SortDirection.DESC))
        }

    }

}
