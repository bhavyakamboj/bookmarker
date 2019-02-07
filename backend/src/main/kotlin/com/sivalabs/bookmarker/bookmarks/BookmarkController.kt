package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.bookmarks.model.TagDTO
import com.sivalabs.bookmarker.exception.BookmarkNotFoundException
import com.sivalabs.bookmarker.utils.SecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController(
    private val bookmarkService: BookmarkService
) {

    @GetMapping
    fun getAllBookmarks(@RequestParam(name = "userId", required = false) userId: Long?): List<BookmarkDTO> {
        return if (userId == null) {
            bookmarkService.getAllBookmarks()
        } else {
            bookmarkService.getBookmarksByUser(userId)
        }
    }

    @GetMapping("/tagged/{tag}")
    fun getBookmarksByTag(@PathVariable tag: String): TagDTO {
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
        bookmark.createdBy = SecurityUtils.loginUser()!!.id
        return bookmarkService.createBookmark(bookmark)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun deleteBookmark(@PathVariable id: Long) {
        val bookmark = bookmarkService.getBookmarkById(id)
        if (bookmark == null || (bookmark.createdBy != SecurityUtils.loginUser()?.id &&
                        !SecurityUtils.isCurrentUserAdmin())) {
            throw BookmarkNotFoundException("Bookmark not found with id=$id")
        } else {
            bookmarkService.deleteBookmark(id)
        }
    }
}
