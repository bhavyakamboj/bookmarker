package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TagController(private val tagRepository: TagRepository) {

    @GetMapping("/tags")
    fun tags(): List<Tag> {
        return tagRepository.findAll()
    }
}
