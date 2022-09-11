package dev.mieser.tsa.web.validator

import org.apache.commons.codec.binary.Base64
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * [ConstraintValidator] validating that a [Base64Encoded] annotated [CharSequence] is a valid Base64
 * String.
 */
class Base64EncodingValidator : ConstraintValidator<Base64Encoded, CharSequence?> {

    /**
     * @param value
     * The [CharSequence] to validate.
     * @param context
     * The context in which the constraint is evaluated.
     * @return `true`, iff the specified value is `null` or a valid Base64 String.
     */
    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        return value == null || Base64.isBase64(value.toString())
    }

}
