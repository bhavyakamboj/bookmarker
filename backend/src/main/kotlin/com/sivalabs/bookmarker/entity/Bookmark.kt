package com.sivalabs.bookmarker.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "bookmarks")
class Bookmark {

    @Id
    @SequenceGenerator(name = "bm_id_generator", sequenceName = "bm_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "bm_id_generator")
    var id: Long = 0

    @Column(nullable = false)
    var url: String = ""

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var liked: Boolean = false

    @Column(nullable = false)
    var archived: Boolean = false

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User = User()

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = LocalDateTime.now()
}
