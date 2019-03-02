package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.domain.repository.BookmarkRepository
import com.sivalabs.bookmarker.domain.repository.TagRepository
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.service.BookmarkService
import com.sivalabs.bookmarker.domain.utils.Constants.DEFAULT_PAGE_SIZE
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Optional

internal class BookmarkServiceTest {

    private val bookmarkRepository = mockk<BookmarkRepository>()

    private val tagRepository = mockk<TagRepository>()

    private val userRepository = mockk<UserRepository>()

    private val bookmarkService =
        BookmarkService(bookmarkRepository, tagRepository, userRepository)

    @Test
    internal fun `should get first page of all bookmarks if page number is not specified`() {
        every { bookmarkRepository.findAll(any<Pageable>()) } answers { Page.empty() }

        bookmarkService.getAllBookmarks()

        val slot = slot<PageRequest>()
        verify { bookmarkRepository.findAll(capture(slot)) }
        assertThat(slot.captured.pageNumber).isEqualTo(0)
        assertThat(slot.captured.pageSize).isEqualTo(DEFAULT_PAGE_SIZE)
    }

    @Test
    internal fun `should get first page of user bookmarks if page number is not specified`() {
        val userId = 1L
        every { bookmarkRepository.findByCreatedById(userId, any()) } answers { Page.empty() }

        bookmarkService.getBookmarksByUser(userId)

        val slot = slot<PageRequest>()
        verify { bookmarkRepository.findByCreatedById(userId, capture(slot)) }
        assertThat(slot.captured.pageNumber).isEqualTo(0)
        assertThat(slot.captured.pageSize).isEqualTo(DEFAULT_PAGE_SIZE)
    }

    @Test
    internal fun `show throw exception while getting bookmarks by tag if tag does not exist`() {
        every { tagRepository.findByName("non-existing-tag") } answers { Optional.empty() }

        assertThatThrownBy { bookmarkService.getBookmarksByTag("non-existing-tag") }
            .isInstanceOf(ResourceNotFoundException::class.java)
    }
}
