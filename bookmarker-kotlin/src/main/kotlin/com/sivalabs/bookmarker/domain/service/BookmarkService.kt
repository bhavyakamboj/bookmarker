package com.sivalabs.bookmarker.domain.service

import com.sivalabs.bookmarker.domain.entity.Bookmark
import com.sivalabs.bookmarker.domain.entity.Tag
import com.sivalabs.bookmarker.domain.exception.TagNotFoundException
import com.sivalabs.bookmarker.domain.model.BookmarkByTagDTO
import com.sivalabs.bookmarker.domain.model.BookmarkDTO
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository
import com.sivalabs.bookmarker.domain.repository.TagRepository
import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.utils.logger
import org.jsoup.Jsoup
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    fun getAllBookmarks(): BookmarksListDTO {
        log.debug("process=get_all_bookmarks")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return buildBookmarksResult(bookmarkRepository.findAll(sort))
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-user")
    fun getBookmarksByUser(userId: Long): BookmarksListDTO {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId")
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return buildBookmarksResult(bookmarkRepository.findByCreatedById(userId, sort))
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-tag")
    fun getBookmarksByTag(tag: String): BookmarkByTagDTO {
        val tagOptional = tagRepository.findByName(tag)
        return tagOptional.map {
            val sort = Sort.by(Sort.Direction.DESC, "createdAt")
            val bookmarks = bookmarkRepository.findByTag(tag, sort)
            val bookmarksResult = buildBookmarksResult(bookmarks)
            BookmarkByTagDTO(
                    id = it.id,
                    name = it.name,
                    bookmarks = bookmarksResult.data
            )
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

    private fun buildBookmarksResult(bookmarks: List<Bookmark>): BookmarksListDTO {
        return BookmarksListDTO(bookmarks.map { BookmarkDTO.fromEntity(it) })
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
