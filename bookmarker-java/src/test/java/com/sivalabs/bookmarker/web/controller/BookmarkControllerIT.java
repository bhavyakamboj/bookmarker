package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookmarkControllerIT extends AbstractIntegrationTest {

    @Test
    void shouldFetchAllBookmarks() throws Exception {
        this.mockMvc.perform(get("/api/bookmarks"))
                .andExpect(status().isOk())
        ;
    }
}
