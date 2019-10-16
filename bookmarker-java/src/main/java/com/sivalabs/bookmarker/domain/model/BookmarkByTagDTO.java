package com.sivalabs.bookmarker.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkByTagDTO {
    private Long id;
    private String name;
    private List<BookmarkDTO> bookmarks;
}
