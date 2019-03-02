package com.sivalabs.bookmarker.domain.model

data class TagDTO(
    val id: Long,
    val name: String,
    val bookmarks: BookmarksResultDTO
)
