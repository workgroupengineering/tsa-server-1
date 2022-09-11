package dev.mieser.tsa.web.controller

import dev.mieser.tsa.web.Slf4jLoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Controller for redirecting HTTP `GET` requests to the Servlet Root Path to another Controller Method.
 */
@Controller
@RequestMapping(path = ["/"], produces = [MediaType.TEXT_HTML_VALUE])
class IndexController {

    private val log by Slf4jLoggerFactory()

    @GetMapping
    fun index(): String {
        log.debug("Redirecting GET request from '/' to '/web/history'.")
        return "redirect:/web/history"
    }

}
