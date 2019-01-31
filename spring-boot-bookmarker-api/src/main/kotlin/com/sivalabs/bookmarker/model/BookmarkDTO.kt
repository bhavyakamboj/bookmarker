package com.sivalabs.bookmarker.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime

interface BookmarkDTO {
    fun getId(): Long

    fun getUrl(): String

    fun getTitle(): String

    @JsonProperty("created_user_id")
    @Value("#{target.createdBy.id}")
    fun getCreatedUserId(): Long

    @JsonProperty("created_user_name")
    @Value("#{target.createdBy.name}")
    fun getCreatedUserName(): String

    @JsonProperty("created_at")
    fun getCreatedAt(): LocalDateTime

    @JsonProperty("updated_at")
    fun getUpdatedAt(): LocalDateTime?
}
