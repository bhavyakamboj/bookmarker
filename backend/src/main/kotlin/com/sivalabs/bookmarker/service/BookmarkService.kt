package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.entity.Bookmark
import com.sivalabs.bookmarker.model.BookmarkDTO
import com.sivalabs.bookmarker.model.BookmarkFilterType
import com.sivalabs.bookmarker.repo.BookmarkRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.jsoup.Jsoup

@Service
@Transactional
class BookmarkService(private val bookmarkRepository: BookmarkRepository) {

    fun getUserBookmarks(userId: Long, filter: BookmarkFilterType): List<BookmarkDTO> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return when(filter) {
            BookmarkFilterType.LIKED -> bookmarkRepository.findByCreatedByIdAndLikedTrue(userId, sort)
            BookmarkFilterType.ARCHIVED -> bookmarkRepository.findByCreatedByIdAndArchivedTrue(userId, sort)
            else  -> bookmarkRepository.findByCreatedById(userId, sort)
        }.map { BookmarkDTO.fromEntity(it) }
    }

    fun getBookmarkById(id: Long): BookmarkDTO? {
        return bookmarkRepository.findById(id)
                .map { BookmarkDTO.fromEntity(it) }
                .orElse(null)
    }

    fun createBookmark(bookmark: Bookmark) {
        if (bookmark.title.isEmpty()) {
            val doc = Jsoup.connect(bookmark.url).get()
            bookmark.title = doc.title()
        }
        bookmarkRepository.save(bookmark)
    }

    fun deleteBookmark(id: Long) {
        bookmarkRepository.deleteById(id)
    }
}
