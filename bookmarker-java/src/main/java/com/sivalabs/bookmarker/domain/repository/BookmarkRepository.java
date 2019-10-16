package com.sivalabs.bookmarker.domain.repository;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByCreatedById(Long userId, Sort sort);

    @Query("select distinct b from Bookmark b join b.tags t where t.name=?1")
    List<Bookmark> findByTag(String tagName, Sort sort);
}
