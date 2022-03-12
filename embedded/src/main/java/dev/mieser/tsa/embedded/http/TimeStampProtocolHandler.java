package dev.mieser.tsa.embedded.http;

import static org.glassfish.grizzly.http.util.HttpStatus.*;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import dev.mieser.tsa.domain.TimeStampResponseData;
import dev.mieser.tsa.signing.api.TimeStampAuthority;
import dev.mieser.tsa.signing.api.exception.UnknownHashAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeStampProtocolHandler extends HttpHandler {

    private final TimeStampAuthority timeStampAuthority;

    @Override
    public void service(Request request, Response response) {
        if (!isValidTspRequest(request)) {
            response.setStatus(BAD_REQUEST_400);
            return;
        }

        try {
            TimeStampResponseData timeStampResponseData = timeStampAuthority.signRequest(request.getInputStream());
            response.getOutputStream().write(timeStampResponseData.getAsnEncoded());
            response.setStatus(OK_200);
        } catch (UnknownHashAlgorithmException e) {
            log.warn("Unknown hash algorithm specified in request.", e);
            response.setStatus(BAD_REQUEST_400);
        } catch (Exception e) {
            log.warn("Failed to sign request.", e);
            response.setStatus(INTERNAL_SERVER_ERROR_500);
        }
    }

    /**
     *
     * @param request
     *     The HTTP request to validate, not {@code null}.
     * @return {@code true}, iff the request is a {@code POST} request with content type
     * {@code application/timestamp-query}.
     */
    private boolean isValidTspRequest(Request request) {
        return "application/timestamp-query".equals(request.getContentType()) && request.getMethod() == Method.POST;
    }

}
