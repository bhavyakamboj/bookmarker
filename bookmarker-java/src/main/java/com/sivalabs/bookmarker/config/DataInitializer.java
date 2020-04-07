package com.sivalabs.bookmarker.config;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final BookmarkerProperties bookmarksProperties;
    private final BookmarkService bookmarkService;

    private static final Long SYSTEM_USER_ID = 1L;

    @Override
    public void run(String... args) throws Exception {
        if(bookmarksProperties.isImportDataEnabled()) {
            bookmarkService.deleteAllBookmarks();
            importBookmarks();
        } else {
            log.info("Data importing is disabled");
        }
    }

    private void importBookmarks() throws IOException, CsvValidationException {
        String fileName = bookmarksProperties.getImportFilePath();
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
