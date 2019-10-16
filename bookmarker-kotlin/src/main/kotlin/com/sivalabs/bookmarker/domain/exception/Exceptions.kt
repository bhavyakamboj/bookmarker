package com.sivalabs.bookmarker.domain.exception

open class BookmarkerException(message: String) : RuntimeException(message)

open class ResourceNotFoundException(message: String) : RuntimeException(message)

class BookmarkNotFoundException(message: String) : ResourceNotFoundException(message)

class UserNotFoundException(message: String) : ResourceNotFoundException(message)

class TagNotFoundException(message: String) : ResourceNotFoundException(message)
