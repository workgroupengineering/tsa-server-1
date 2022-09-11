package dev.mieser.tsa.web.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Custom Bean Validation annotation validating that a [Number] is a valid TCP Port.
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [TcpPortValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class TcpPort(

    /**
     * @return The validation message.
     */
    val message: String = "{dev.mieser.tsa.web.validator.TcpPort.message}",

    /**
     * @return The groups this validation belongs to. Default is an empty array.
     */
    val groups: Array<KClass<*>> = [],

    /**
     * @return Additional payload for the validation. Ignored.
     */
    val payload: Array<KClass<out Payload>> = []

)
