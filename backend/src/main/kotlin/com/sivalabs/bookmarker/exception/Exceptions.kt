package com.sivalabs.bookmarker.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
open class ResourceNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookmarkNotFoundException(message: String) : ResourceNotFoundException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String) : ResourceNotFoundException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IncorrectPasswordException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ResourceAlreadyExistException(message: String) : RuntimeException(message)
