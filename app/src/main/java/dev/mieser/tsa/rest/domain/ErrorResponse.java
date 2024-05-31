package dev.mieser.tsa.rest.domain;

/**
 * Defines the general structure for API error responses.
 */
public interface ErrorResponse {

    /**
     * @return The HTTP status code.
     */
    int status();

    /**
     * @return A message to describe what went wrong.
     */
    String message();

}
