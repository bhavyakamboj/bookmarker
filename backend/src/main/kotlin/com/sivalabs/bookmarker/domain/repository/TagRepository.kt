package com.sivalabs.bookmarker.domain.repository

import com.sivalabs.bookmarker.domain.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(tag: String): Optional<Tag>
}
