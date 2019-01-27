package com.sivalabs.bookmarker.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.FetchType
import javax.persistence.ManyToMany

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

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    var roles: MutableList<Role> = mutableListOf()

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()
}
