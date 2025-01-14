package dev.mieser.tsa.rest;

import static jakarta.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import dev.mieser.tsa.domain.TimeStampResponseData;
import dev.mieser.tsa.integration.api.DeleteTimestampResponseService;
import dev.mieser.tsa.integration.api.QueryTimeStampResponseService;
import dev.mieser.tsa.persistence.api.Page;
import dev.mieser.tsa.persistence.api.PageRequest;
import dev.mieser.tsa.rest.converter.SortQueryParamConverter;
import dev.mieser.tsa.rest.domain.BasicErrorResponse;
import dev.mieser.tsa.rest.domain.ConstraintViolationResponse;
import dev.mieser.tsa.rest.domain.HttpStatusCode;

@Transactional
@Path("/history/responses")
@RequiredArgsConstructor
public class ResponseHistoryResource {

    private final QueryTimeStampResponseService queryTimeStampResponseService;

    private final DeleteTimestampResponseService deleteTimestampResponseService;

    private final SortQueryParamConverter sortQueryParamConverter;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
                     responseCode = HttpStatusCode.OK,
                     description = "When the response with the specified ID was found."),
        @APIResponse(
                     responseCode = HttpStatusCode.NOT_FOUND,
                     description = "When the response with the specified ID was not found.")
    })
    public TimeStampResponseData findById(@PathParam("id") long id) {
        return queryTimeStampResponseService.findById(id)
            .orElseThrow(this::buildNotFoundException);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
                     responseCode = HttpStatusCode.OK,
                     description = "The specified page with the configured size."),
        @APIResponse(
                     responseCode = HttpStatusCode.BAD_REQUEST,
                     description = "When a validation constraint is violated.",
                     content = @Content(schema = @Schema(implementation = ConstraintViolationResponse.class)))
    })
    public Page<TimeStampResponseData> findAll(
        @DefaultValue("1") @QueryParam("page") @Min(1) int page,
        @DefaultValue("50") @QueryParam("size") @Min(1) @Max(500) int size,
        @Pattern(regexp = SortQueryParamConverter.PATTERN, flags = CASE_INSENSITIVE) @QueryParam("sort") String sort) {
        return queryTimeStampResponseService.findAll(new PageRequest(page, size, sortQueryParamConverter.fromString(sort)));
    }

    @DELETE
    @Path("/{id}")
    @APIResponses({
        @APIResponse(
                     responseCode = HttpStatusCode.NO_CONTENT,
                     description = "When the saved response was deleted successfully."),
        @APIResponse(
                     responseCode = HttpStatusCode.NOT_FOUND,
                     description = "When the response with the specified ID was not found.")
    })
    public Response deleteById(@PathParam("id") long id) {
        boolean deleted = deleteTimestampResponseService.deleteById(id);
        if (!deleted) {
            throw buildNotFoundException();
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("/")
    @APIResponses({
        @APIResponse(
                     responseCode = HttpStatusCode.NO_CONTENT,
                     description = "When all responses were deleted successfully.")
    })
    public Response deleteAll() {
        deleteTimestampResponseService.deleteAll();
        return Response.noContent().build();
    }

    /**
     * @implNote According to the JAX-RS Specification (chapter 3.3.4), a {@link WebApplicationException} must contain a
     * response to be handled by the standard exception handlers.
     */
    private NotFoundException buildNotFoundException() {
        var response = Response.status(NOT_FOUND)
            .entity(new BasicErrorResponse(NOT_FOUND.getStatusCode()))
            .build();

        return new NotFoundException(response);
    }

}
