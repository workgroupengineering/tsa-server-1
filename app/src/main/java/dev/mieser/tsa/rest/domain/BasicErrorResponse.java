package dev.mieser.tsa.rest.domain;

/**
 * @see ErrorResponse
 */
public record BasicErrorResponse(int status, String message) implements ErrorResponse {

    public BasicErrorResponse(int status) {
        this(status, null);
    }

}
