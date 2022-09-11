package dev.mieser.tsa.web.controller

import dev.mieser.tsa.integration.api.IssueTimeStampService
import dev.mieser.tsa.signing.api.exception.InvalidTspRequestException
import dev.mieser.tsa.signing.api.exception.TspResponseException
import dev.mieser.tsa.signing.api.exception.UnknownHashAlgorithmException
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

/**
 * [RestController] for answering [RFC 3161](https://www.ietf.org/rfc/rfc3161.txt) Time Stamp Requests.
 *
 * @see IssueTimeStampService
 */
@Transactional
@RestController
@RequestMapping(path = ["/"])
open class TimeStampAuthorityController(private val issueTimeStampService: IssueTimeStampService) {

    @PostMapping(consumes = ["application/timestamp-query"], produces = ["application/timestamp-reply"])
    open fun sign(requestInputStream: InputStream): ResponseEntity<ByteArray> {
        val responseData = issueTimeStampService.signTimestampRequest(requestInputStream)
        return ResponseEntity.ok(responseData.asnEncoded)
    }

    @ExceptionHandler(InvalidTspRequestException::class, UnknownHashAlgorithmException::class)
    open fun handleRequestExceptions(): ResponseEntity<*> {
        return ResponseEntity.badRequest()
                .build<Unit>()
    }

    @ExceptionHandler(TspResponseException::class)
    open fun handleServerExceptions(): ResponseEntity<*> {
        return ResponseEntity.internalServerError()
                .build<Unit>()
    }

}
