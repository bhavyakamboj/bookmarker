package com.sivalabs.bookmarker.entity

import javax.persistence.*
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.FetchType
import javax.persistence.ManyToMany

@Entity
@Table(name = "users")
class User : BaseEntity() {

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
}
