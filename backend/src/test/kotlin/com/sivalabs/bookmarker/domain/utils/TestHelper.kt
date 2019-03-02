package com.sivalabs.bookmarker.domain.utils

import com.sivalabs.bookmarker.domain.entity.Bookmark
import com.sivalabs.bookmarker.domain.entity.Tag
import com.sivalabs.bookmarker.domain.entity.User
import java.util.Random
import java.util.UUID

object TestHelper {
    const val DEFAULT_PASSWORD = "secret"

    fun buildUser(withUserId: Boolean = false): User {
        val userId = if (withUserId) Random().nextLong() else 0
        val uuid = UUID.randomUUID().toString()
        val user = User()
        user.id = userId
        user.name = "name-$uuid"
        user.email = "someone-$uuid@gmail.com"
        user.password = DEFAULT_PASSWORD
        return user
    }

    fun buildBookmark(withId: Boolean = false): Bookmark {
        val id = if (withId) Random().nextLong() else 0
        val uuid = UUID.randomUUID().toString()
        return buildBookmark(id, "http://$uuid.com", "title-$uuid")
    }

    fun buildBookmark(id: Long? = null, url: String, title: String = "", vararg tags: String): Bookmark {
        val bookmark = Bookmark()
        bookmark.id = id ?: 0
        bookmark.url = url
        bookmark.title = title
        val tagList = tags.map {
            val t = Tag()
            t.name = it
            t
        }
        bookmark.tags = tagList.toMutableList()
        return bookmark
    }
}