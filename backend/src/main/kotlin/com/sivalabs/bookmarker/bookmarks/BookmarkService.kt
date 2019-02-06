package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Bookmark
import com.sivalabs.bookmarker.bookmarks.entity.Tag
import com.sivalabs.bookmarker.bookmarks.entity.toDTO
import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.users.UserRepository
import com.sivalabs.bookmarker.utils.logger
import org.jsoup.Jsoup
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookmarkService(
    private val bookmarkRepository: BookmarkRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {

    private val log = logger()

    @Transactional(readOnly = true)
    fun getAllBookmarks(): List<BookmarkDTO> {
        log.debug("process=get_all_bookmarks")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findAll(sort).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun getBookmarksByUser(userId: Long): List<BookmarkDTO> {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return bookmarkRepository.findByCreatedById(userId, sort).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun getBookmarkById(id: Long): BookmarkDTO? {
        log.debug("process=get_bookmark_by_id, id=$id")
        return bookmarkRepository.findById(id)
                .map { it.toDTO() }
                .orElse(null)
    }

    fun createBookmark(bookmark: BookmarkDTO): BookmarkDTO {
        log.debug("process=create_bookmark, url=${bookmark.url}")
        return saveBookmark(bookmark).toDTO()
    }

    fun deleteBookmark(id: Long) {
        log.debug("process=delete_bookmark_by_id, id=$id")
        bookmarkRepository.deleteById(id)
    }

    private fun saveBookmark(bookmarkDTO: BookmarkDTO): Bookmark {
        val bookmark = Bookmark()
        bookmark.url = bookmarkDTO.url
        bookmark.title = bookmarkDTO.title
        if (bookmark.title.isEmpty()) {
            val doc = Jsoup.connect(bookmark.url).get()
            bookmark.title = doc.title()
        }
        bookmark.createdBy = userRepository.getOne(bookmarkDTO.createdBy)
        val tagsList = mutableListOf<Tag>()
        bookmarkDTO.tags.forEach { tagName ->
            if (tagName.trim() != "") {
                val tag = createTagIfNotExist(tagName.trim())
                tagsList.add(tag)
            }
        }
        bookmark.tags = tagsList
        return bookmarkRepository.save(bookmark)
    }

    private fun createTagIfNotExist(tagName: String): Tag {
        val tagOptional = tagRepository.findByName(tagName)
        if (tagOptional.isPresent) {
            return tagOptional.get()
        }
        val tag = Tag()
        tag.name = tagName
        return tagRepository.save(tag)
    }
}
