package com.sivalabs.bookmarker.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarksListDTO {
    private List<BookmarkDTO> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;
    @JsonProperty("isFirst")
    boolean isFirst;
    @JsonProperty("isLast")
    boolean isLast;
    @JsonProperty("hasNext")
    boolean hasNext;
    @JsonProperty("hasPrevious")
    boolean hasPrevious;

    public BookmarksListDTO(Page<BookmarkDTO> bookmarksPage) {
        this.setData(bookmarksPage.getContent());
        this.setTotalElements(bookmarksPage.getTotalElements());
        this.setPageNumber(bookmarksPage.getNumber()+1); // 1 - based page numbering
        this.setTotalPages(bookmarksPage.getTotalPages());
        this.setFirst(bookmarksPage.isFirst());
        this.setHasNext(bookmarksPage.hasNext());
        this.setHasPrevious(bookmarksPage.hasPrevious());
    }
}
