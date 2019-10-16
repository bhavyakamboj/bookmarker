package com.sivalabs.bookmarker.domain.service

import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository
import com.sivalabs.bookmarker.domain.repository.TagRepository
import com.sivalabs.bookmarker.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort
import java.util.Optional

internal class BookmarkServiceTest {

    private val bookmarkRepository = mockk<BookmarkRepository>()

    private val tagRepository = mockk<TagRepository>()

    private val userRepository = mockk<UserRepository>()

    private val bookmarkService =
            BookmarkService(bookmarkRepository, tagRepository, userRepository)

    @Test
    internal fun `should get all bookmarks`() {
        every { bookmarkRepository.findAll(any<Sort>()) } answers { listOf() }

        bookmarkService.getAllBookmarks()

        verify { bookmarkRepository.findAll(any<Sort>()) }
    }

    @Test
    internal fun `should get user bookmarks`() {
        val userId = 1L
        every { bookmarkRepository.findByCreatedById(userId, any()) } answers { listOf() }

        bookmarkService.getBookmarksByUser(userId)

        verify { bookmarkRepository.findByCreatedById(userId, any()) }
    }

    @Test
    internal fun `show throw exception while getting bookmarks by tag if tag does not exist`() {
        every { tagRepository.findByName("non-existing-tag") } answers { Optional.empty() }

        assertThatThrownBy { bookmarkService.getBookmarksByTag("non-existing-tag") }
                .isInstanceOf(ResourceNotFoundException::class.java)
    }
}
