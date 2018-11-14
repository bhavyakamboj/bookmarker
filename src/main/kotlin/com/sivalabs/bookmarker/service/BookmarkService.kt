package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.entity.Bookmark
import com.sivalabs.bookmarker.model.BookmarkDTO
import com.sivalabs.bookmarker.repo.BookmarkRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookmarkService(private val bookmarkRepository: BookmarkRepository) {

    fun getBookmarks(): List<BookmarkDTO> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findAll(sort).map { BookmarkDTO.fromEntity(it) }
    }

    fun getBookmarkById(id: Long): BookmarkDTO? {
        return bookmarkRepository.findById(id)
                .map { BookmarkDTO.fromEntity(it) }
                .orElse(null)
    }

    fun createBookmark(bookmark: Bookmark) {
        bookmarkRepository.save(bookmark)
    }

    fun deleteBookmark(id: Long) {
        bookmarkRepository.deleteById(id)
    }
}
