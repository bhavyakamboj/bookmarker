package com.sivalabs.bookmarker.service

import com.sivalabs.bookmarker.entity.Bookmark
import com.sivalabs.bookmarker.model.BookmarkDTO
import com.sivalabs.bookmarker.repo.BookmarkRepository
import com.sivalabs.bookmarker.utils.logger
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.jsoup.Jsoup

@Service
@Transactional(readOnly = true)
class BookmarkService(private val bookmarkRepository: BookmarkRepository) {
    private val log = logger()

    fun getAllBookmarks(): List<BookmarkDTO> {
        log.debug("process=get_all_bookmarks")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findAllBookmarks(sort)
    }

    fun getBookmarksByUser(userId: Long): List<BookmarkDTO> {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findByCreatedById(userId, sort)
    }

    fun getBookmarkById(id: Long): BookmarkDTO? {
        log.debug("process=get_bookmark_by_id, id=$id")
        return bookmarkRepository.findBookmarkById(id)
                .orElse(null)
    }

    @Transactional(readOnly = false)
    fun createBookmark(bookmark: Bookmark) {
        log.debug("process=create_bookmark, url=${bookmark.url}")
        if (bookmark.title.isEmpty()) {
            val doc = Jsoup.connect(bookmark.url).get()
            bookmark.title = doc.title()
        }
        bookmarkRepository.save(bookmark)
    }

    @Transactional(readOnly = false)
    fun deleteBookmark(id: Long) {
        log.debug("process=delete_bookmark_by_id, id=$id")
        bookmarkRepository.deleteById(id)
    }
}
