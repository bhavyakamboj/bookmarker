package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.domain.entity.Tag
import com.sivalabs.bookmarker.domain.repository.TagRepository
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
