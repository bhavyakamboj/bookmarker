package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.domain.model.BookmarkDTO
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO
import com.sivalabs.bookmarker.domain.model.BookmarkByTagDTO
import com.sivalabs.bookmarker.domain.service.BookmarkService
import com.sivalabs.bookmarker.domain.exception.BookmarkNotFoundException
import com.sivalabs.bookmarker.domain.utils.Constants
import com.sivalabs.bookmarker.web.utils.SecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController(
    private val bookmarkService: BookmarkService
) {

    @GetMapping
    fun getAllBookmarks(
        @RequestParam(name = "userId", required = false) userId: Long?,
        @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(name = "size", required = false, defaultValue = "${Constants.DEFAULT_PAGE_SIZE}") size: Int
    ): BookmarksListDTO {
        return if (userId == null) {
            bookmarkService.getAllBookmarks(page, size)
        } else {
            bookmarkService.getBookmarksByUser(userId, page, size)
        }
    }

    @GetMapping("/tagged/{tag}")
    fun getBookmarksByTag(@PathVariable("tag") tag: String): BookmarkByTagDTO {
        return bookmarkService.getBookmarksByTag(tag)
    }

    @GetMapping("/{id}")
    fun getBookmarkById(@PathVariable id: Long): ResponseEntity<BookmarkDTO> {
        return bookmarkService.getBookmarkById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createBookmark(@RequestBody bookmark: BookmarkDTO): BookmarkDTO {
        bookmark.createdUserId = SecurityUtils.loginUser()!!.id
        return bookmarkService.createBookmark(bookmark)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun deleteBookmark(@PathVariable id: Long) {
        val bookmark = bookmarkService.getBookmarkById(id)
        if (bookmark == null || (bookmark.createdUserId != SecurityUtils.loginUser()?.id &&
                    !SecurityUtils.isCurrentUserAdmin())
        ) {
            throw BookmarkNotFoundException("Bookmark not found with id=$id")
        } else {
            bookmarkService.deleteBookmark(id)
        }
    }
}
