package com.sivalabs.bookmarker.repo

import com.sivalabs.bookmarker.entity.Bookmark
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findByCreatedById(userId: Long, sort: Sort): List<Bookmark>
    fun findByCreatedByIdAndLikedTrue(userId: Long, sort: Sort): List<Bookmark>
    fun findByCreatedByIdAndArchivedTrue(userId: Long, sort: Sort): List<Bookmark>
}
