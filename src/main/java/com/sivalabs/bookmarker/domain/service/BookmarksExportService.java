package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarksExportService {
    private final BookmarkService bookmarkService;

    public byte[] getBookmarksCSVFileAsString() {
        StringBuilder sb = new StringBuilder();
        List<BookmarkDTO> allBookmarks = bookmarkService.getAllBookmarks();
        for (BookmarkDTO bookmark : allBookmarks) {
            sb.append(bookmark.getUrl()+",")
                    .append("\""+bookmark.getTitle()+"\""+",")
                    .append(String.join("|", bookmark.getTags()))
                    .append(System.lineSeparator());
        }
        return sb.toString().getBytes();
    }
}
