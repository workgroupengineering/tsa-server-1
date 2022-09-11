package dev.mieser.tsa.web.controller

import dev.mieser.tsa.web.Slf4jLoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(path = ["/web/history"], produces = [MediaType.TEXT_HTML_VALUE])
class HistoryController {

    val log by Slf4jLoggerFactory()

    @GetMapping
    fun history(): String {
        try {
            throw IllegalStateException("Test-Exception")
        } catch (e : Exception) {
            log.warn("WTF an exception occurred!!!!1!!", e)
        }

        return "history"
    }

}
