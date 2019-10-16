package com.sivalabs.bookmarker.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "roles")
class Role : BaseEntity() {

    @Id
    @SequenceGenerator(name = "role_id_generator", sequenceName = "role_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "role_id_generator")
    var id: Long = 0

    @Column(nullable = false)
    var name: String = ""

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    var users: MutableList<User> = mutableListOf()
}
