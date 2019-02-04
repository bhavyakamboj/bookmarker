package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.bookmarks.entity.Bookmark
import com.sivalabs.bookmarker.utils.logger
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.jsoup.Jsoup

@Service
@Transactional
class BookmarkService(private val bookmarkRepository: BookmarkRepository) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun getAllBookmarks(): List<BookmarkDTO> {
        log.debug("process=get_all_bookmarks")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findAll(sort).map { BookmarkDTO.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getBookmarksByUser(userId: Long): List<BookmarkDTO> {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findByCreatedById(userId, sort).map { BookmarkDTO.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getBookmarkById(id: Long): BookmarkDTO? {
        log.debug("process=get_bookmark_by_id, id=$id")
        return bookmarkRepository.findById(id)
                .map { BookmarkDTO.fromEntity(it) }
                .orElse(null)
    }

    fun createBookmark(bookmark: Bookmark) {
        log.debug("process=create_bookmark, url=${bookmark.url}")
        if (bookmark.title.isEmpty()) {
            val doc = Jsoup.connect(bookmark.url).get()
            bookmark.title = doc.title()
        }
        bookmarkRepository.save(bookmark)
    }

    fun deleteBookmark(id: Long) {
        log.debug("process=delete_bookmark_by_id, id=$id")
        bookmarkRepository.deleteById(id)
    }
}
