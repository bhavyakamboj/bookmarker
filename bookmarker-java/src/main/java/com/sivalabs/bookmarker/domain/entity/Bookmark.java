package com.sivalabs.bookmarker.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bookmarks")
@Setter
@Getter
public class Bookmark extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "bm_id_generator", sequenceName = "bm_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "bm_id_generator")
    private Long id;

    @Column(nullable=false)
    @NotEmpty()
    private String url;

    @Column(nullable=false)
    @NotEmpty()
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "bookmark_tag",
        joinColumns = {@JoinColumn(name = "bookmark_id", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "ID")}
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

}
