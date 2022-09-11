package dev.mieser.tsa.web.dto.datatable

import com.fasterxml.jackson.annotation.JsonCreator

/**
 * The sort directions
 */
enum class SortDirection {

    /**
     * Ascending
     */
    ASC,

    /**
     * Descending
     */
    DESC;

    companion object {

        /**
         * Jackson factory method to map the lower-case names of Datatables to enum constants.
         *
         * @param direction
         * The name of the enum constant to return.
         * @return The corresponding enum constant or `null`, when the specified direction is `null`.
         * @throws IllegalArgumentException
         * When no enum constant was found.
         */
        @JsonCreator
        @JvmStatic
        fun fromDirection(direction: String): SortDirection {
            return SortDirection.values()
                    .find { sortDirection -> sortDirection.name.equals(direction, ignoreCase = true) }
                    .let { it ?: throw IllegalArgumentException("No direction constant found for '$direction'.") }
        }

    }

}
