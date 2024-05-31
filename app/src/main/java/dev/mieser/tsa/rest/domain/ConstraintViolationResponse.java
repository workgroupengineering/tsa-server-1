package dev.mieser.tsa.rest.domain;

import java.util.List;

/**
 * {@link ErrorResponse} when the Jakarta Bean Validation fails.
 */
public record ConstraintViolationResponse(
    int status,
    String message,
    List<ConstraintViolationResponseEntity> violations) implements ErrorResponse {

    public record ConstraintViolationResponseEntity(String field, String message) {

    }

}
