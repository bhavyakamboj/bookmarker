package com.sivalabs.bookmarker.bookmarks.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "tags")
class Tag {

    @Id
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tag_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "tag_id_generator")
    var id: Long = 0

    @Column(nullable = false, unique = true)
    var name: String = ""

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    var bookmarks: MutableList<Bookmark> = mutableListOf()

    override fun toString(): String {
        return "Tag(id=$id, name='$name')"
    }
}
