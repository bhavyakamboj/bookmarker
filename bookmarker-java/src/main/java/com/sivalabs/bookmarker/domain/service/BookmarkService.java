package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.exception.TagNotFoundException;
import com.sivalabs.bookmarker.domain.model.BookmarkByTagDTO;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.model.BookmarksListDTO;
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository;
import com.sivalabs.bookmarker.domain.repository.TagRepository;
import com.sivalabs.bookmarker.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks")
    public BookmarksListDTO getAllBookmarks()  {
        log.debug("process=get_all_bookmarks");
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return buildBookmarksResult(bookmarkRepository.findAll(sort));
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-user")
    public BookmarksListDTO getBookmarksByUser(Long userId) {
        log.debug("process=get_bookmarks_by_user_id, user_id=$userId");
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return buildBookmarksResult(bookmarkRepository.findByCreatedById(userId, sort));
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmarks-by-tag")
    public BookmarkByTagDTO getBookmarksByTag(String tag)  {
        Optional<Tag> tagOptional = tagRepository.findByName(tag);
        if(!tagOptional.isPresent()) {
            throw new TagNotFoundException("Tag $tag not found");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Bookmark> bookmarks = bookmarkRepository.findByTag(tag, sort);
        BookmarksListDTO bookmarksResult = buildBookmarksResult(bookmarks);
        return new BookmarkByTagDTO(
                tagOptional.get().getId(),
                tagOptional.get().getName(),
                bookmarksResult.getData()
        );
    }

    @Transactional(readOnly = true)
    @Cacheable("bookmark-by-id")
    public Optional<BookmarkDTO> getBookmarkById(Long id) {
        log.debug("process=get_bookmark_by_id, id=$id");
        return bookmarkRepository.findById(id).map(BookmarkDTO::fromEntity);
    }

    @CacheEvict(value = {"bookmarks", "bookmarks-by-tag", "bookmarks-by-user"}, allEntries = true)
    public BookmarkDTO createBookmark(BookmarkDTO bookmark) throws IOException {
        log.debug("process=create_bookmark, url=${bookmark.url}");
        return BookmarkDTO.fromEntity(saveBookmark(bookmark));
    }

    @CacheEvict(value = {"bookmarks", "bookmark-by-id", "bookmarks-by-tag", "bookmarks-by-user"}, allEntries = true)
    public void deleteBookmark(Long id) {
        log.debug("process=delete_bookmark_by_id, id=$id");
        bookmarkRepository.deleteById(id);
    }

    private BookmarksListDTO buildBookmarksResult(List<Bookmark> bookmarks)  {
        return new BookmarksListDTO(bookmarks.stream().map(BookmarkDTO::fromEntity).collect(Collectors.toList()));
    }

    private Bookmark saveBookmark(BookmarkDTO bookmarkDTO) throws IOException {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(bookmarkDTO.getUrl());
        bookmark.setTitle(bookmarkDTO.getTitle());

        if (bookmark.getTitle().isEmpty()) {
            Document doc = Jsoup.connect(bookmark.getUrl()).get();
            bookmark.setTitle(doc.title());
        }
        bookmark.setCreatedBy(userRepository.getOne(bookmarkDTO.getCreatedUserId()));
        List<Tag> tagsList = new ArrayList<>();
        bookmarkDTO.getTags().forEach(tagName -> {
            if (!tagName.trim().isEmpty()) {
                Tag tag = createTagIfNotExist(tagName.trim());
                tagsList.add(tag);
            }
        });
        bookmark.setTags(tagsList);
        return bookmarkRepository.save(bookmark);
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
