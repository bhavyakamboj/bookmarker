package com.sivalabs.bookmarker.domain.service;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.sivalabs.bookmarker.domain.utils.Constants.SYSTEM_USER_ID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarksImportService {
    private final BookmarkService bookmarkService;

    public void importBookmarks(String fileName) throws IOException, CsvValidationException {
        log.info("Importing bookmarks from file: {}", fileName);
        CSVIterator iterator = getCsvIteratorFromClassPathResource(fileName);
        long count = 0L;

        while (iterator.hasNext()) {
            String[] nextLine = iterator.next();
            BookmarkDTO bookmarkDTO = new BookmarkDTO();
            bookmarkDTO.setUrl(nextLine[0]);
            bookmarkDTO.setTitle(nextLine[1]);
            bookmarkDTO.setCreatedUserId(SYSTEM_USER_ID);
            bookmarkDTO.setCreatedAt(LocalDateTime.now());
            if(nextLine.length > 2 && StringUtils.trimToNull(nextLine[2]) != null) {
                bookmarkDTO.setTags(Arrays.asList(StringUtils.trimToEmpty(nextLine[2]).split("\\|")));
            }
            bookmarkService.createBookmark(bookmarkDTO);
            count++;
        }
        log.info("Imported {} bookmarks from file {}", count, fileName);
    }

    private CSVIterator getCsvIteratorFromClassPathResource(String fileName) throws IOException, CsvValidationException {
        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.skip(1);
        return new CSVIterator(csvReader);
    }
}
