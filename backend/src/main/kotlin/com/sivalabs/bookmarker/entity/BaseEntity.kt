package com.sivalabs.bookmarker.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Version

@MappedSuperclass
abstract class BaseEntity {
    @JsonProperty("created_at")
    @Column(insertable = true, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @JsonProperty("updated_at")
    @Column(insertable = false, updatable = true)
    var updatedAt: LocalDateTime? = LocalDateTime.now()

    @JsonIgnore
    @Version
    var version: Long = 0

    @PrePersist
    fun onCreate() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
