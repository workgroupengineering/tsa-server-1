package dev.mieser.tsa.rest.providers;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import dev.mieser.tsa.rest.domain.BasicErrorResponse;

/**
 * {@link ExceptionMapper} for all unhandled exceptions.
 */
@Provider
@Priority(Integer.MAX_VALUE)
public class InternalErrorResponseMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.serverError()
            .entity(new BasicErrorResponse(INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage()))
            .build();
    }

}
