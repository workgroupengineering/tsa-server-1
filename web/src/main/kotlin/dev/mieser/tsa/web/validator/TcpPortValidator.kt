package dev.mieser.tsa.web.validator

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] validating that a [TcpPort] annotated [Number] is a valid TCP Port.
 */
class TcpPortValidator : ConstraintValidator<TcpPort, Number> {

    /**
     * @param value The value to validate.
     * @param context The validation context.
     * @return `true` iff the [value] is `null` or a valid TCP port between `1` and `65,535`.
     */
    override fun isValid(value: Number?, context: ConstraintValidatorContext?): Boolean {
        return value == null || value in LOWER_BOUND..UPPER_BOUND
    }

    companion object {

        const val LOWER_BOUND = 1

        const val UPPER_BOUND = 65_535

    }

}
