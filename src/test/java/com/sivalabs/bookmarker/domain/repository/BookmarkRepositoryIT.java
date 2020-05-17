package com.sivalabs.bookmarker.domain.repository;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sivalabs.bookmarker.utils.Constants.PROFILE_IT;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(PROFILE_IT)
class BookmarkRepositoryIT {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    void shouldReturnAllBookmarks() {
        List<Bookmark> allBookmarks = bookmarkRepository.findAll();
        assertThat(allBookmarks).isNotNull();
    }
}
