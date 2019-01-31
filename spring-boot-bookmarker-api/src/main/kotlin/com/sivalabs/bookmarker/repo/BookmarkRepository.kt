package com.sivalabs.bookmarker.repo

import com.sivalabs.bookmarker.entity.Bookmark
import com.sivalabs.bookmarker.model.BookmarkDTO
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {

    @Query("select b from Bookmark b")
    fun findAllBookmarks(sort: Sort): List<BookmarkDTO>

    @Query("select b from Bookmark b")
    fun findByCreatedById(userId: Long, sort: Sort): List<BookmarkDTO>

    @Query("select b from Bookmark b where b.id = ?1")
    fun findBookmarkById(userId: Long): Optional<BookmarkDTO>
}
