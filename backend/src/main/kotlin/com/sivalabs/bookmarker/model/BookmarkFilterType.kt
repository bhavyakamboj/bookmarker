package com.sivalabs.bookmarker.model

import java.lang.Exception

enum class BookmarkFilterType {
    LIKED,
    ARCHIVED,
    NO_FILTER;

    companion object {
        fun fromString(filter: String): BookmarkFilterType {
            return try {
                BookmarkFilterType.valueOf(filter.toUpperCase())
            } catch (e: Exception) {
                NO_FILTER
            }
        }
    }
}
