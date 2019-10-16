package com.sivalabs.bookmarker.domain.repository;

import com.sivalabs.bookmarker.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tag);
}
