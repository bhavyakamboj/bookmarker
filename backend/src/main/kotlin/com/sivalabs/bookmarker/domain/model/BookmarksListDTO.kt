package com.sivalabs.bookmarker.domain.model

data class BookmarksListDTO(
    val content: List<BookmarkDTO>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int
)
