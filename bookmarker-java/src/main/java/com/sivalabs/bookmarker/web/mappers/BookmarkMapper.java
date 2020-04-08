package com.sivalabs.bookmarker.web.mappers;

import com.sivalabs.bookmarker.domain.entity.Bookmark;
import com.sivalabs.bookmarker.domain.entity.Tag;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookmarkMapper {
    private final SecurityService securityService;

    public BookmarkDTO toDTO(Bookmark bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setUrl(bookmark.getUrl());
        dto.setTitle(bookmark.getTitle());
        dto.setCreatedUserId(bookmark.getCreatedBy().getId());
        dto.setCreatedUserName(bookmark.getCreatedBy().getName());
        dto.setCreatedAt(bookmark.getCreatedAt());
        dto.setUpdatedAt(bookmark.getUpdatedAt());
        if(bookmark.getTags() != null) {
            dto.setTags(bookmark.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        }
        boolean editable = securityService.canCurrentUserEditBookmark(dto);
        dto.setEditable(editable);
        return dto;
    }

}
