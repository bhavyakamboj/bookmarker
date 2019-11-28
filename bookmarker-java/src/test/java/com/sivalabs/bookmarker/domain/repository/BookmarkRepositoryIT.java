package com.sivalabs.bookmarker.domain.repository;

import com.sivalabs.bookmarker.common.PostgreSQLContainerInitializer;
import com.sivalabs.bookmarker.domain.entity.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import javax.persistence.EntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
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