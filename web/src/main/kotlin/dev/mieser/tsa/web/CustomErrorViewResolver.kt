package dev.mieser.tsa.web

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

/**
 * Spring Boot [ErrorViewResolver] which uses the same error view for all HTTP status codes.
 */
@Component
class CustomErrorViewResolver : ErrorViewResolver {

    override fun resolveErrorView(request: HttpServletRequest, status: HttpStatus, model: Map<String, Any>): ModelAndView {
        validateRequiredModelFieldsArePresent(model)
        return ModelAndView("error", model)
    }

    /**
     * @param model
     * The model to validate, not `null`.
     */
    private fun validateRequiredModelFieldsArePresent(model: Map<String, Any>) {
        requireNotNull(model["status"]) { "HTTP Status Code not present in model." }
    }

}
