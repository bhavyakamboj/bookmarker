package com.sivalabs.bookmarker.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "tags")
@Setter
@Getter
public class Tag extends BaseEntity {

    @Id
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tag_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "tag_id_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty()
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Bookmark> bookmarks;
}
