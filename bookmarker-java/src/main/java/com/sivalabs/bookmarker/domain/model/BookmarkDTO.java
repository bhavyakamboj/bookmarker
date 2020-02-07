package com.sivalabs.bookmarker.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.bookmarker.domain.entity.Bookmark;
import com.sivalabs.bookmarker.domain.entity.Tag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookmarkDTO {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;

    @JsonProperty("created_user_id")
    private Long createdUserId;

    @JsonProperty("created_user_name")
    private String createdUserName;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private List<String> tags;

    public List<String> getTags() {
        if(this.tags == null) {
            this.tags = new ArrayList<>();
        }
        return this.tags;
    }

    public static BookmarkDTO fromEntity(Bookmark bookmark) {
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
        return dto;
    }
}
