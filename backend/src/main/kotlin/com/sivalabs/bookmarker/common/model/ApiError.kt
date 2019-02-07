package com.sivalabs.bookmarker.common.model

data class ApiError(
    val message: String,
    val exception: Exception? = null
)
