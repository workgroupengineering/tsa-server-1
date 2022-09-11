package dev.mieser.tsa.web.dto

import dev.mieser.tsa.web.validator.Base64Encoded
import javax.validation.constraints.NotEmpty

/**
 * DTO encapsulating information about TSP responses.
 *
 * @param base64EncodedResponse The Base64 encoding of an ASN.1 DER encoded TSP response.
 */
data class TimeStampResponseDto(@field:[NotEmpty(message = "{dev.mieser.tsa.web.dto.TimeStampResponseDto.notEmpty}") Base64Encoded] val base64EncodedResponse: String? = null)
