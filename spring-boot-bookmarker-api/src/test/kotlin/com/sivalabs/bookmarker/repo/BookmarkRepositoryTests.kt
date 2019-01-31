package com.sivalabs.bookmarker.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class BookmarkRepositoryTests {

    @Autowired
    lateinit var bookmarkRepo: BookmarkRepository

    @Test
    fun `should fetch all bookmarks`() {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val bookmarks = bookmarkRepo.findAllBookmarks(sort)
        assertThat(bookmarks).isNotEmpty
    }
}
