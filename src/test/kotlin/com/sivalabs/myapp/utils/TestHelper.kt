package com.sivalabs.myapp.utils

import com.sivalabs.myapp.model.UserDTO
import java.util.Random
import java.util.UUID

object TestHelper {

    fun buildUser(withUserId: Boolean = false): UserDTO {
        val userId = if (withUserId) Random().nextLong() else 0
        val uuid = UUID.randomUUID().toString()
        return UserDTO(userId, "name-$uuid", "someone-$uuid@gmail.com", "ghuser-$uuid")
    }

    fun getClasspathResourceContent(filepath: String) =
            TestHelper::class.java.javaClass.getResource(filepath).readText()
}
