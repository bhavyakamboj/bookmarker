package com.sivalabs.myapp.utils

import com.sivalabs.myapp.model.UserDTO
import java.util.*

object TestHelper {

    fun buildUser(): UserDTO {
        val uuid = UUID.randomUUID().toString()
        return UserDTO(0, "name-$uuid", "someone-$uuid@gmail.com", "ghuser-$uuid")
    }

    fun buildUserWithId(): UserDTO {
        val random = Random()
        val uuid = UUID.randomUUID().toString()
        return UserDTO(random.nextLong(), "name-$uuid", "someone-$uuid@gmail.com", "ghuser-$uuid")
    }

    fun getClasspathResourceContent(filepath: String) =
            TestHelper::class.java.javaClass.getResource(filepath).readText()
}
