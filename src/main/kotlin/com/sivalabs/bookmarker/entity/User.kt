package com.sivalabs.bookmarker.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.*

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

    @Column(name = "password", nullable = false)
    var password: String = ""

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.ROLE_USER

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()
}
