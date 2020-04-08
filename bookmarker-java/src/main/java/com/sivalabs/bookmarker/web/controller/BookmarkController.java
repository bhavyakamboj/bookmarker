package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.service.BookmarkService;
import com.sivalabs.bookmarker.domain.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasRole('ROLE_USER')")
    public String newBookmarkForm(Model model) {
        model.addAttribute("bookmark", new BookmarkDTO());
        return "add-bookmark";
    }

    @PostMapping("/bookmarks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createBookmark(@Valid @ModelAttribute("bookmark") BookmarkDTO bookmark,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-bookmark";
        }
        bookmark.setCreatedUserId(securityService.loginUserId());
        bookmarkService.createBookmark(bookmark);
        return "redirect:/bookmarks";
    }

    @GetMapping("/bookmarks/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editBookmarkForm(@PathVariable Long id, Model model) {
        BookmarkDTO bookmark = bookmarkService.getBookmarkById(id).orElse(null);
        if (bookmark == null ||
                !(bookmark.getCreatedUserId().equals(securityService.loginUserId())
                || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Bookmark not found with id="+id);
        } else {
            model.addAttribute("bookmark", bookmark);
        }
        return "edit-bookmark";
    }

    @PutMapping("/bookmarks/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateBookmark(@PathVariable Long id,
                                 @Valid @ModelAttribute("bookmark") BookmarkDTO bookmark,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-bookmark";
        }
        bookmark.setId(id);
        bookmark.setCreatedUserId(securityService.loginUserId());
        bookmarkService.updateBookmark(bookmark);
        return "redirect:/bookmarks";
    }

    @DeleteMapping("/bookmarks/{id}")
    @ResponseStatus
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        BookmarkDTO bookmark = bookmarkService.getBookmarkById(id).orElse(null);
        if (bookmark == null ||
                !(bookmark.getCreatedUserId().equals(securityService.loginUserId())
                || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Bookmark not found with id="+id);
        } else {
            bookmarkService.deleteBookmark(id);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks/download")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void downloadBookmarks(HttpServletResponse response) throws IOException {
        String filename = "bookmarks.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        StringBuilder sb = new StringBuilder();
        List<BookmarkDTO> allBookmarks = bookmarkService.getAllBookmarks();
        for (BookmarkDTO bookmark : allBookmarks) {
            sb.append(bookmark.getUrl()+",")
                    .append("\""+bookmark.getTitle()+"\""+",")
                    .append(String.join("|", bookmark.getTags()))
                    .append(System.lineSeparator());
        }
        response.getWriter().write(sb.toString());
    }

    @GetMapping("/page-metadata")
    @ResponseStatus
    public ResponseEntity<Map<String, String>> getPageMetadata(@RequestParam String url) {
        Map<String, String> metadata = new HashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            metadata.put("title", doc.title());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(metadata);
    }

}
