package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Bookmark
import com.sivalabs.bookmarker.bookmarks.entity.Tag
import com.sivalabs.bookmarker.bookmarks.entity.toDTO
import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.bookmarks.model.BookmarksResultDTO
import com.sivalabs.bookmarker.bookmarks.model.TagDTO
import com.sivalabs.bookmarker.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.users.UserRepository
import com.sivalabs.bookmarker.utils.Constants.DEFAULT_PAGE_SIZE
import com.sivalabs.bookmarker.utils.logger
import org.jsoup.Jsoup
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
    fun getAllBookmarks(page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): BookmarksResultDTO {
        log.debug("process=get_all_bookmarks, pageNo=$page, size=$size")
        val pageable = buildPageRequest(page, size)
        return buildBookmarksResult(bookmarkRepository.findAll(pageable))
    }

    @Transactional(readOnly = true)
    fun getBookmarksByUser(userId: Long, page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): BookmarksResultDTO {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId, pageNo=$page, size=$size")
        val pageable = buildPageRequest(page, size)
        return buildBookmarksResult(bookmarkRepository.findByCreatedById(userId, pageable))
    }

    @Transactional(readOnly = true)
    fun getBookmarksByTag(tag: String, page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): TagDTO {
        val tagOptional = tagRepository.findByName(tag)
        return tagOptional.map {
            val pageable = buildPageRequest(page, size)
            val bookmarksPage = bookmarkRepository.findByTag(tag, pageable)
            TagDTO(it.id, it.name, buildBookmarksResult(bookmarksPage))
        }.orElseThrow { ResourceNotFoundException("Tag $tag not found") }
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

    private fun buildPageRequest(page: Int, size: Int): PageRequest {
        // from client perspective page number starts from 1
        val pageNo = if (page < 1) 0 else page - 1
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return PageRequest.of(pageNo, size, sort)
    }

    private fun buildBookmarksResult(page: Page<Bookmark>): BookmarksResultDTO {
        return BookmarksResultDTO(
            content = page.content.map { it.toDTO() },
            currentPage = page.number + 1,
            totalElements = page.totalElements,
            totalPages = page.totalPages
        )
    }

    private fun saveBookmark(bookmarkDTO: BookmarkDTO): Bookmark {
        val bookmark = Bookmark()
        bookmark.url = bookmarkDTO.url
        bookmark.title = bookmarkDTO.title
        if (bookmark.title.isEmpty()) {
            val doc = Jsoup.connect(bookmark.url).get()
            bookmark.title = doc.title()
        }
        bookmark.createdBy = userRepository.getOne(bookmarkDTO.createdUserId)
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
