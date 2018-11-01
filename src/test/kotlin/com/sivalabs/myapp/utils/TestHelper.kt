package com.sivalabs.myapp.utils

import com.sivalabs.myapp.model.UserDTO

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Random
import java.util.UUID
import java.util.stream.Stream

import java.lang.String.format

object TestHelper {

    fun buildUser(): UserDTO {
        val uuid = UUID.randomUUID().toString()
        val dto = UserDTO()
        dto.name = "name-$uuid"
        dto.email = format("someone-%s@gmail.com", uuid)
        dto.githubUsername = "ghuser-$uuid"
        return dto
    }

    fun buildUserWithId(): UserDTO {
        val random = Random()
        val uuid = UUID.randomUUID().toString()
        val dto = UserDTO()
        dto.id = random.nextLong()
        dto.name = "name-$uuid"
        dto.email = format("someone-%s@gmail.com", uuid)
        dto.githubUsername = "ghuser-$uuid"
        return dto
    }

    fun getClasspathResourceContent(filepath: String): String {
        try {
            val path = Paths.get(TestHelper::class.java.javaClass.getResource(filepath).toURI())

            val data = StringBuilder()
            val lines = Files.lines(path)
            lines.forEach { line -> data.append(line).append("\n") }
            lines.close()
            return data.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }
}
