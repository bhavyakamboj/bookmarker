package com.sivalabs.bookmarker.domain.repository;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select distinct b from Bookmark b join b.tags t where t.name=?1")
    Page<Bookmark> findByTag(String tagName, Pageable pageable);

    Page<Bookmark> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}
