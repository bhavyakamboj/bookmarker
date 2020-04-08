package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.domain.service.BookmarksExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookmarksDownloadController {
    private final BookmarksExportService bookmarksExportService;

    @GetMapping("/bookmarks/download")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void downloadBookmarks(HttpServletResponse response) throws IOException {
        String filename = "bookmarks.csv";
        String mimeType = "text/csv";
        byte[] csvData = bookmarksExportService.getBookmarksCSVFileAsString();
        response.setContentType(mimeType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(csvData);
    }
}
