package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.annotations.AdminOnly;
import com.sivalabs.bookmarker.config.BookmarkerProperties;
import com.sivalabs.bookmarker.domain.model.email.Email;
import com.sivalabs.bookmarker.domain.model.email.EmailAttachment;
import com.sivalabs.bookmarker.domain.service.BookmarksExportService;
import com.sivalabs.bookmarker.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookmarksEmailingController {
    private final BookmarksExportService bookmarksExportService;
    private final EmailService emailService;
    private final BookmarkerProperties bookmarkerProperties;

    @GetMapping("/bookmarks/email")
    @AdminOnly
    @ResponseStatus
    public void emailBookmarks() {
        String filename = "bookmarks.csv";
        String mimeType = "text/csv";
        byte[] csvData = bookmarksExportService.getBookmarksCSVFileAsString();
        try {
            Email email = new Email();
            //email.setFrom(bookmarkerProperties.getAdminEmail());
            email.setSubject(getSubject());
            email.addTo(bookmarkerProperties.getAdminEmail());
            email.setText("Hi, \nPlease find the attached Bookmarks CSV File");

            EmailAttachment attachment = new EmailAttachment(csvData, filename, mimeType, false);
            email.addAttachment(attachment);

            emailService.sendEmail(email);
            log.info("Bookmarks emailed to {} successfully", bookmarkerProperties.getAdminEmail());
        } catch (Exception e) {
            log.error("Failed to email bookmarks", e);
        }
    }

    private String getSubject() {
        return "Bookmarks CSV-" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    }
}
