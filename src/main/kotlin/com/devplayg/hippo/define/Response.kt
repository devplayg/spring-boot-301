package com.devplayg.hippo.define

import org.springframework.http.HttpStatus

/**
 * HTTP Response 객체 설계
 * Hints from : https://elizarov.medium.com/kotlin-and-exceptions-8062f589d07
 */

sealed class Response {
    data class Success(
            var data: Any,
            val success: Boolean = true,
            val status: HttpStatus = HttpStatus.OK
    ) : Response()

    data class Failure(
            val message: String,
            val success: Boolean = false,
            val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ) : Response()

    data class Updated(
            val affectedRows: Int = 0,
            val success: Boolean = true,
            val status: HttpStatus = HttpStatus.OK
    ) : Response()

    data class Created(
            val lastInsertId: Int = 0,
            val affectedRows: Int = 1,
            val success: Boolean = false,
            val status: HttpStatus = HttpStatus.OK
    ) : Response()

    data class Found(
            val data: Any,
            val success: Boolean = true,
            val status: HttpStatus = HttpStatus.OK
    ) : Response()

    data class NotFound(
            val success: Boolean = false,
            val message: String = "Not found",
            val status: HttpStatus = HttpStatus.NOT_FOUND
    ) : Response()

    fun httpStatus(): HttpStatus {
        if (this is Success) {
            return this.status
        }
        if (this is Failure) {
            return this.status
        }
        if (this is NotFound) {
            return this.status
        }
        if (this is Found) {
            return this.status
        }
        if (this is Updated) {
            return this.status
        }
        if (this is Created) {
            return this.status
        }
        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}
