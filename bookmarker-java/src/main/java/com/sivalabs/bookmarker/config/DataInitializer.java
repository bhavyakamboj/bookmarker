package com.sivalabs.bookmarker.config;

import com.sivalabs.bookmarker.domain.service.BookmarkService;
import com.sivalabs.bookmarker.domain.service.BookmarksImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final BookmarkerProperties bookmarksProperties;
    private final BookmarkService bookmarkService;
    private final BookmarksImportService bookmarksImportService;

    @Override
    public void run(String... args) throws Exception {
        if(bookmarksProperties.isImportDataEnabled()) {
            bookmarkService.deleteAllBookmarks();
            String fileName = bookmarksProperties.getImportFilePath();
            bookmarksImportService.importBookmarks(fileName);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
