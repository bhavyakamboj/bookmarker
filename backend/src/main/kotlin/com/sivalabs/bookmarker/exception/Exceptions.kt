package com.sivalabs.bookmarker.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
open class ResourceNotFoundException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : ResourceNotFoundException(message)
