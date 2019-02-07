package com.sivalabs.bookmarker.bookmarks.model

data class BookmarksResultDTO(
    val content: List<BookmarkDTO>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int
)
