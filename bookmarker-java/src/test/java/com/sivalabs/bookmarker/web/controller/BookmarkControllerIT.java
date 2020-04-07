package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookmarkControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchBookmarksFirstPage() throws Exception {
        this.mockMvc.perform(get("/bookmarks"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void shouldFetchBookmarksSecondPage() throws Exception {
        this.mockMvc.perform(get("/bookmarks?page=2"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void shouldFetchBookmarksByTag() throws Exception {
        this.mockMvc.perform(get("/bookmarks?tag=spring-boot"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void shouldSearchBookmarks() throws Exception {
        this.mockMvc.perform(get("/bookmarks?query=spring"))
                .andExpect(status().isOk())
        ;
    }
}
