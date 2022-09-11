package dev.mieser.tsa.web.filter

import dev.mieser.tsa.web.Slf4jLoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Servlet [Filter] to verify that HTTP servlet requests received on a specific port are HTTP `POST`
 * requests with the content type `application/timestamp-query`. If the port matches, but the HTTP request is not
 * a `POST` request with content type `application/timestamp-query`, the response status is set to
 * `405` (Method not allowed) or `415` (Unsupported Media Type) respectively.
 *
 * @param tspHandlerPort The TCP port which accepts Time Stamp Protocol requests.
 */
class TimeStampProtocolFilter(private val tspHandlerPort: Int) : Filter {

    private val log by Slf4jLoggerFactory()

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (isRequestToTspHandlerPort(request)) {
            if (!isPostRequest(request)) {
                log.info("HTTP request with unsupported method '{}' received on TSP handler port '{}'.",
                        (request as HttpServletRequest).method, tspHandlerPort)
                (response as HttpServletResponse).status = HttpStatus.METHOD_NOT_ALLOWED.value()
                return
            } else if (!isTimeStampQueryRequest(request)) {
                log.info("HTTP post request with illegal content type '{}' received on TSP handler port '{}'.",
                        request.contentType, tspHandlerPort)
                (response as HttpServletResponse).status = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()
                return
            }
        }
        chain.doFilter(request, response)
    }

    /**
     * @param request
     * The servlet request to check, not `null`.
     * @return `true`, iff the content type is `application/timestamp-query`.
     */
    private fun isTimeStampQueryRequest(request: ServletRequest): Boolean {
        if (!request.contentType.isNullOrBlank()) {
            return TIME_STAMP_QUERY_MEDIA_TYPE.isCompatibleWith(MediaType.parseMediaType(request.contentType))
        }

        return false;
    }

    /**
     * @param request
     * The HTTP request to check, not `null`.
     * @return `true`, iff the request is a {@value HTTP_POST_METHOD} request.
     */
    private fun isPostRequest(request: ServletRequest): Boolean {
        val httpRequest = request as HttpServletRequest
        return httpRequest.method == HTTP_POST_METHOD
    }

    /**
     * @param request
     * The servlet request to check, not `null`.
     * @return `true`, iff the servlet request was received on the configured TSP handler port.
     */
    private fun isRequestToTspHandlerPort(request: ServletRequest): Boolean {
        return request.serverPort == tspHandlerPort
    }

    companion object {

        /**
         * The media type of Time Stamp Protocol HTTP requests.
         */
        private val TIME_STAMP_QUERY_MEDIA_TYPE = MediaType.parseMediaType("application/timestamp-query")

        /**
         * The name of the HTTP method of `POST` requests.
         */
        private const val HTTP_POST_METHOD = "POST"

    }

}
