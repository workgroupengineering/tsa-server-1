package dev.mieser.tsa.rest.providers;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import dev.mieser.tsa.rest.domain.ConstraintViolationResponse;
import dev.mieser.tsa.rest.domain.ConstraintViolationResponse.ConstraintViolationResponseEntity;

@Provider
public class BeanConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        ConstraintViolationResponse responseEntity = new ConstraintViolationResponse(
            BAD_REQUEST.getStatusCode(),
            e.getMessage(),
            extractViolationResponseEntities(e));

        return Response.status(BAD_REQUEST)
            .entity(responseEntity)
            .build();
    }

    private List<ConstraintViolationResponseEntity> extractViolationResponseEntities(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
            .map(this::map)
            .toList();
    }

    private ConstraintViolationResponseEntity map(ConstraintViolation<?> violation) {
        return new ConstraintViolationResponseEntity(
            violation.getPropertyPath().toString(),
            violation.getMessage());
    }

}
