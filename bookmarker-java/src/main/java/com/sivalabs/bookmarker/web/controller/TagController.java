package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {
    private final TagRepository tagRepository;

    @GetMapping("/tags")
    public List<Tag> allTags() {
        return tagRepository.findAll();
    }
}
