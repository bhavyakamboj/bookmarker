package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.domain.exception.BookmarkNotFoundException;
import com.sivalabs.bookmarker.domain.model.BookmarkByTagDTO;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.service.BookmarkService;
import com.sivalabs.bookmarker.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping
    public BookmarksListDTO getAllBookmarks(@RequestParam(name = "userId", required = false) Long userId) {
        if (userId == null) {
            return bookmarkService.getAllBookmarks();
        } else {
            return bookmarkService.getBookmarksByUser(userId);
        }
    }

    @GetMapping("/tagged/{tag}")
    public BookmarkByTagDTO getBookmarksByTag(@PathVariable("tag") String tag) {
        return bookmarkService.getBookmarksByTag(tag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmarkById(@PathVariable Long id) {
        return bookmarkService.getBookmarkById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookmarkDTO createBookmark(@RequestBody BookmarkDTO bookmark) throws IOException {
        bookmark.setCreatedUserId(SecurityUtils.loginUser().getId());
        return bookmarkService.createBookmark(bookmark);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteBookmark(@PathVariable Long id) {
        BookmarkDTO bookmark = bookmarkService.getBookmarkById(id).orElse(null);
        if (bookmark == null || (!bookmark.getCreatedUserId().equals(SecurityUtils.loginUser().getId()) &&
                !SecurityUtils.isCurrentUserAdmin())
        ) {
            throw new BookmarkNotFoundException("Bookmark not found with id=$id");
        } else {
            bookmarkService.deleteBookmark(id);
        }
    }
}
