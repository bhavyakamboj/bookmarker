package com.sivalabs.bookmarker.bookmarks.entity

import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.common.entity.BaseEntity
import com.sivalabs.bookmarker.users.entity.User
import javax.persistence.*

@Entity
@Table(name = "bookmarks")
class Bookmark : BaseEntity() {

    @Id
    @SequenceGenerator(name = "bm_id_generator", sequenceName = "bm_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "bm_id_generator")
    var id: Long = 0

    @Column(nullable = false)
    var url: String = ""

    @Column(nullable = false)
    var title: String = ""

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(name = "bookmark_tag", joinColumns = [JoinColumn(name = "bookmark_id", referencedColumnName = "ID")],
            inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "ID")])
    var tags: MutableList<Tag> = mutableListOf()

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User = User()
}

fun Bookmark.toDTO() = BookmarkDTO.fromEntity(this)
