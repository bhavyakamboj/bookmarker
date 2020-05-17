package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.annotations.AnyAuthenticatedUser;
import com.sivalabs.bookmarker.annotations.CurrentUser;
import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.entity.User;
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.service.BookmarkService;
import com.sivalabs.bookmarker.domain.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final SecurityService securityService;

    private static final String PAGINATION_PREFIX = "paginationPrefix";

    @ModelAttribute("tags")
    public List<Tag> allTags() {
        return bookmarkService.findAllTags();
    }

    @GetMapping("/bookmarks")
    public String home(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @PageableDefault(size = 15)
            @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)}) Pageable pageable,
            Model model) {
        BookmarksListDTO data;
        if(StringUtils.isNotEmpty(tag)) {
            log.info("Fetching bookmarks for tag {} with page: {}", tag, pageable.getPageNumber());
            data = bookmarkService.getBookmarksByTag(tag, pageable);
            model.addAttribute("header", "Bookmarks by Tag : "+ tag);
            model.addAttribute(PAGINATION_PREFIX, "/bookmarks?tag="+tag);
        } else if(StringUtils.isNotEmpty(query)) {
            log.info("Searching bookmarks for {} with page: {}", query, pageable.getPageNumber());
            data = bookmarkService.searchBookmarks(query, pageable);
            model.addAttribute("header", "Search Results for : "+ query);
            model.addAttribute(PAGINATION_PREFIX, "/bookmarks?query="+query);
        } else {
            log.info("Fetching bookmarks with page: {}", pageable.getPageNumber());
            data = bookmarkService.getAllBookmarks(pageable);
            model.addAttribute(PAGINATION_PREFIX, "/bookmarks?");
        }
        model.addAttribute("bookmarksData", data);
        return "home";
    }

    @GetMapping("/bookmarks/new")
    @AnyAuthenticatedUser
    public String newBookmarkForm(Model model) {
        model.addAttribute("bookmark", new BookmarkDTO());
        return "add-bookmark";
    }

    @PostMapping("/bookmarks")
    @AnyAuthenticatedUser
    public String createBookmark(@Valid @ModelAttribute("bookmark") BookmarkDTO bookmark,
                                 @CurrentUser User loginUser,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-bookmark";
        }
        bookmark.setCreatedUserId(loginUser.getId());
        bookmarkService.createBookmark(bookmark);
        return "redirect:/bookmarks";
    }

    @GetMapping("/bookmarks/edit/{id}")
    @AnyAuthenticatedUser
    public String editBookmarkForm(@PathVariable Long id,
                                   @CurrentUser User loginUser,
                                   Model model) {
        BookmarkDTO bookmark = bookmarkService.getBookmarkById(id).orElse(null);
        this.checkPrivilege(id, bookmark, loginUser);
        model.addAttribute("bookmark", bookmark);
        return "edit-bookmark";
    }

    @PutMapping("/bookmarks/{id}")
    @AnyAuthenticatedUser
    public String updateBookmark(@PathVariable Long id,
                                 @Valid @ModelAttribute("bookmark") BookmarkDTO bookmark,
                                 @CurrentUser User loginUser,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-bookmark";
        }
        this.checkPrivilege(id, bookmark, loginUser);
        bookmark.setId(id);
        bookmark.setCreatedUserId(loginUser.getId());
        bookmarkService.updateBookmark(bookmark);
        return "redirect:/bookmarks";
    }

    @DeleteMapping("/bookmarks/{id}")
    @ResponseStatus
    @AnyAuthenticatedUser
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id, @CurrentUser User loginUser) {
        BookmarkDTO bookmark = bookmarkService.getBookmarkById(id).orElse(null);
        this.checkPrivilege(id, bookmark, loginUser);
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Long bookmarkId, BookmarkDTO bookmark, User loginUser) {
        if (bookmark == null ||
                !(bookmark.getCreatedUserId().equals(loginUser.getId())
                        || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Bookmark not found with id="+bookmarkId);
        }
    }
}
