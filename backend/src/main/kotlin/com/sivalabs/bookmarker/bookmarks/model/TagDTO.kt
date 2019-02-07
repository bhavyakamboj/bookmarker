package com.sivalabs.bookmarker.bookmarks.model

data class TagDTO(
    val id: Long,
    val name: String,
    val bookmarks: List<BookmarkDTO>
)
