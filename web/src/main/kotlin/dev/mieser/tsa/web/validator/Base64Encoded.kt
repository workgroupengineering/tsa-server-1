package dev.mieser.tsa.web.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Custom Bean Validation annotation validating that a [CharSequence] is a valid Base64 String.
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [Base64EncodingValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class Base64Encoded(

        /**
         * @return The validation message.
         */
        val message: String = "{dev.mieser.tsa.web.validator.Base64Encoded.message}",

        /**
         * @return The groups this validation belongs to. Default is an empty array.
         */
        val groups: Array<KClass<*>> = [],

        /**
         * @return Additional payload for the validation. Ignored.
         */
        val payload: Array<KClass<out Payload>> = []

)
