package com.sivalabs.bookmarker.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.sivalabs.bookmarker.domain.entity.Bookmark
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class BookmarkDTO(
    var id: Long = 0,

    @field:NotBlank(message = "URL cannot be blank")
    var url: String = "",

    var title: String = "",

    @JsonProperty("created_user_id")
    var createdUserId: Long = 0,

    @JsonProperty("created_user_name")
    var createdUserName: String = "",

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    var tags: List<String> = listOf()
) {
    companion object {
        fun fromEntity(bm: Bookmark) = BookmarkDTO(
            bm.id,
            bm.url,
            bm.title,
            bm.createdBy.id,
            bm.createdBy.name,
            bm.createdAt,
            bm.updatedAt,
            bm.tags.map { it.name }
        )
    }
}
