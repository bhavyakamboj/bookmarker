package com.sivalabs.myapp.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "users")
class User {

    @Id
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_id_generator")
    var id: Long = 0

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false, unique = true)
    var email: String = ""

    @Column(name = "gh_username")
    var githubUsername: String = ""

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()
}
