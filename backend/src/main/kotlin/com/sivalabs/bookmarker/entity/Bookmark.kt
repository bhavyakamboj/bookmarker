package com.sivalabs.bookmarker.entity

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

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User = User()
}
