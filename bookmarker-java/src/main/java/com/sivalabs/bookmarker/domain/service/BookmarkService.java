package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository;
import com.sivalabs.bookmarker.domain.repository.TagRepository;
import com.sivalabs.bookmarker.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BookmarkDTO> getAllBookmarks()  {
        return bookmarkRepository.findAll()
                .stream().map(BookmarkDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookmarksListDTO getAllBookmarks(Pageable pageable)  {
        return buildBookmarksResult(bookmarkRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public BookmarksListDTO searchBookmarks(String query, Pageable pageable) {
        return buildBookmarksResult(bookmarkRepository.findByTitleContainingIgnoreCase(query, pageable));
    }

    @Transactional(readOnly = true)
    public BookmarksListDTO getBookmarksByTag(String tag, Pageable pageable)  {
        Optional<Tag> tagOptional = tagRepository.findByName(tag);
        if(!tagOptional.isPresent()) {
            throw new ResourceNotFoundException("Tag "+ tag + " not found");
        }
        return buildBookmarksResult(bookmarkRepository.findByTag(tag, pageable));
    }

    @Transactional(readOnly = true)
    public Optional<BookmarkDTO> getBookmarkById(Long id) {
        log.debug("process=get_bookmark_by_id, id={}", id);
        return bookmarkRepository.findById(id).map(BookmarkDTO::fromEntity);
    }

    public BookmarkDTO createBookmark(BookmarkDTO bookmark) {
        bookmark.setId(null);
        log.debug("process=create_bookmark, url={}", bookmark.getUrl());
        return BookmarkDTO.fromEntity(saveBookmark(bookmark));
    }

    public BookmarkDTO updateBookmark(BookmarkDTO bookmark) {
        log.debug("process=update_bookmark, url={}", bookmark.getUrl());
        return BookmarkDTO.fromEntity(saveBookmark(bookmark));
    }

    public void deleteBookmark(Long id) {
        log.debug("process=delete_bookmark_by_id, id={}", id);
        bookmarkRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllTags() {
        Sort sort = Sort.by("name");
        return tagRepository.findAll(sort);
    }

    private BookmarksListDTO buildBookmarksResult(Page<Bookmark> bookmarks)  {
        log.trace("Found {} bookmarks in page", bookmarks.getNumberOfElements());
        return new BookmarksListDTO(bookmarks.map(BookmarkDTO::fromEntity));
    }

    private Bookmark saveBookmark(BookmarkDTO bookmarkDTO) {
        Bookmark bookmark = new Bookmark();
        if(bookmarkDTO.getId() != null) {
            bookmark = bookmarkRepository.findById(bookmarkDTO.getId()).orElse(new Bookmark());
        }
        bookmark.setUrl(bookmarkDTO.getUrl());
        bookmark.setTitle(getTitle(bookmarkDTO));
        bookmark.setCreatedBy(userRepository.getOne(bookmarkDTO.getCreatedUserId()));
        bookmark.setCreatedAt(LocalDateTime.now());
        Set<Tag> tagsList = new HashSet<>();
        bookmarkDTO.getTags().forEach(tagName -> {
            if (!tagName.trim().isEmpty()) {
                Tag tag = createTagIfNotExist(tagName.trim());
                tagsList.add(tag);
            }
        });
        bookmark.setTags(tagsList);
        return bookmarkRepository.save(bookmark);
    }

    private String getTitle(BookmarkDTO bookmark) {
        if (StringUtils.isNotEmpty(bookmark.getTitle())) {
            return bookmark.getTitle();
        }
        try {
            Document doc = Jsoup.connect(bookmark.getUrl()).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return bookmark.getUrl();
    }

    private Tag createTagIfNotExist(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findByName(tagName);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        }
        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.save(tag);
    }

}
