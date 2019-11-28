package com.sivalabs.bookmarker.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookmarker.common.AbstractUnitTest;
import com.sivalabs.bookmarker.config.security.TokenHelper;
import com.sivalabs.bookmarker.domain.entity.Bookmark;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookmarkController.class)
class BookmarkControllerTest extends AbstractUnitTest {
    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenHelper tokenHelper;

    @MockBean
    private SecurityProblemSupport problemSupport;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Bookmark> bookmarkList = null;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());

        bookmarkList = new ArrayList<>();
    }

    @Test
    @WithAnonymousUser
    void shouldFetchAllBookmarks() throws Exception {
        BookmarksListDTO bookmarksListDTO = new BookmarksListDTO();
        given(bookmarkService.getAllBookmarks()).willReturn(bookmarksListDTO);

        this.mockMvc.perform(get("/api/bookmarks"))
                .andExpect(status().isOk())
        ;
    }
}