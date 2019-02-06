package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(tag: String): Optional<Tag>
}
