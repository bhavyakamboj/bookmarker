package com.sivalabs.bookmarker.domain.repository

import com.sivalabs.bookmarker.domain.entity.Bookmark
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {

    fun findByCreatedById(userId: Long, sort: Sort): List<Bookmark>

    @Query("select distinct b from Bookmark b join b.tags t where t.name=?1")
    fun findByTag(tagName: String, sort: Sort): List<Bookmark>
}
