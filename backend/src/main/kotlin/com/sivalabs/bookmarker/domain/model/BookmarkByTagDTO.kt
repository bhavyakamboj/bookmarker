package com.sivalabs.bookmarker.domain.model

data class BookmarkByTagDTO(
    val id: Long,
    val name: String,
    val bookmarks: List<BookmarkDTO>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int
)
