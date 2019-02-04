package com.sivalabs.bookmarker.utils

import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.entity.Bookmark
import java.util.Random
import java.util.UUID

object TestHelper {

    fun buildUser(withUserId: Boolean = false): User {
        val userId = if (withUserId) Random().nextLong() else 0
        val uuid = UUID.randomUUID().toString()
        val user = User()
        user.id = userId
        user.name = "name-$uuid"
        user.email = "someone-$uuid@gmail.com"
        user.password = "pwd-$uuid"
        return user
    }

    fun buildBookmark(withId: Boolean = false): Bookmark {
        val id = if (withId) Random().nextLong() else 0
        val uuid = UUID.randomUUID().toString()
        val bookmark = Bookmark()
        bookmark.id = id
        bookmark.url = "http://$uuid.com"
        bookmark.title = "title-$uuid"
        return bookmark
    }
}
