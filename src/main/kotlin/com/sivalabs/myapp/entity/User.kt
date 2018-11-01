package com.sivalabs.myapp.entity

import com.fasterxml.jackson.annotation.JsonProperty

import javax.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User {

    @Id
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_id_generator")
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(name = "gh_username")
    var githubUsername: String? = null

    @JsonProperty("created_at")
    var createdAt: LocalDateTime? = null

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = null

    @PrePersist
    internal fun preSave() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now()
        }
    }

    @PreUpdate
    internal fun preUpdate() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now()
        }
    }
}
