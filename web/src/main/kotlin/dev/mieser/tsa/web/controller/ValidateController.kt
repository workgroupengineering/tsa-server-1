package dev.mieser.tsa.web.controller

import dev.mieser.tsa.integration.api.ValidateTimeStampResponseService
import dev.mieser.tsa.signing.api.exception.InvalidTspResponseException
import dev.mieser.tsa.web.dto.TimeStampResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@Controller
@RequestMapping(path = ["/web/validate"], produces = [MediaType.TEXT_HTML_VALUE])
class ValidateController(private val validateTimeStampResponseService: ValidateTimeStampResponseService) {

    @GetMapping
    fun validateResponse(@ModelAttribute("response") response: TimeStampResponseDto): String {
        return "validate"
    }

    @PostMapping
    fun validationResult(@Valid @ModelAttribute("response") response: TimeStampResponseDto, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            return "validate"
        }
        model.addAttribute("validationResult",
                validateTimeStampResponseService.validateTimeStampResponse(response.base64EncodedResponse))
        return "validation-result"
    }

    @ExceptionHandler(InvalidTspResponseException::class)
    fun handleInvalidTspResponse(): ModelAndView {
        val model = mapOf(
                "invalidTspResponse" to true,
                "response" to TimeStampResponseDto()
        )
        return ModelAndView("validate", model, HttpStatus.BAD_REQUEST)
    }

}
