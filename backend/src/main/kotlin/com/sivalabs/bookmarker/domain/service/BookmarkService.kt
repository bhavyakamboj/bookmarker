package com.sivalabs.bookmarker.domain.service

import com.sivalabs.bookmarker.domain.entity.Bookmark
import com.sivalabs.bookmarker.domain.entity.Tag
import com.sivalabs.bookmarker.domain.model.BookmarkDTO
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO
import com.sivalabs.bookmarker.domain.model.BookmarkByTagDTO
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository
import com.sivalabs.bookmarker.domain.repository.TagRepository
import com.sivalabs.bookmarker.domain.exception.TagNotFoundException
import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.utils.Constants.DEFAULT_PAGE_SIZE
import com.sivalabs.bookmarker.domain.utils.logger
import org.jsoup.Jsoup
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    @Cacheable("bookmarks")
    fun getAllBookmarks(page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): BookmarksListDTO {
        log.debug("process=get_all_bookmarks, pageNo=$page, size=$size")
        val pageable = buildPageRequest(page, size)
        return buildBookmarksResult(bookmarkRepository.findAll(pageable))
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-user")
    fun getBookmarksByUser(userId: Long, page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): BookmarksListDTO {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId, pageNo=$page, size=$size")
        val pageable = buildPageRequest(page, size)
        return buildBookmarksResult(bookmarkRepository.findByCreatedById(userId, pageable))
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-tag")
    fun getBookmarksByTag(tag: String, page: Int = 1, size: Int = DEFAULT_PAGE_SIZE): BookmarkByTagDTO {
        val tagOptional = tagRepository.findByName(tag)
        return tagOptional.map {
            val pageable = buildPageRequest(page, size)
            val bookmarksPage = bookmarkRepository.findByTag(tag, pageable)
            val bookmarksResult = buildBookmarksResult(bookmarksPage)
            BookmarkByTagDTO(
                    id = it.id,
                    name = it.name,
                    bookmarks = bookmarksResult.content,
                    totalElements = bookmarksResult.totalElements,
                    totalPages = bookmarksResult.totalPages,
                    currentPage = bookmarksResult.currentPage)
        }.orElseThrow { TagNotFoundException("Tag $tag not found") }
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmark-by-id")
    fun getBookmarkById(id: Long): BookmarkDTO? {
        log.debug("process=get_bookmark_by_id, id=$id")
        return bookmarkRepository.findById(id)
            .map { BookmarkDTO.fromEntity(it) }
            .orElse(null)
    }

    @CacheEvict("bookmarks", "bookmarks-by-tag", "bookmarks-by-user")
    fun createBookmark(bookmark: BookmarkDTO): BookmarkDTO {
        log.debug("process=create_bookmark, url=${bookmark.url}")
        return BookmarkDTO.fromEntity(saveBookmark(bookmark))
    }

    @CacheEvict("bookmarks", "bookmark-by-id", "bookmarks-by-tag", "bookmarks-by-user")
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

    private fun buildBookmarksResult(page: Page<Bookmark>): BookmarksListDTO {
        return BookmarksListDTO(
            content = page.content.map { BookmarkDTO.fromEntity(it) },
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
