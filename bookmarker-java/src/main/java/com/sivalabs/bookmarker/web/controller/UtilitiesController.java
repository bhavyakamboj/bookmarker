package com.sivalabs.bookmarker.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UtilitiesController {

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
