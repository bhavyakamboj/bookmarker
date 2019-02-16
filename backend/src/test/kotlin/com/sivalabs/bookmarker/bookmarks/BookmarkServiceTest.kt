package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.users.UserRepository
import com.sivalabs.bookmarker.utils.Constants.DEFAULT_PAGE_SIZE
import com.sivalabs.bookmarker.utils.any
import com.sivalabs.bookmarker.utils.argumentCaptor
import com.sivalabs.bookmarker.utils.capture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class BookmarkServiceTest {

    @Mock
    private lateinit var bookmarkRepository: BookmarkRepository

    @Mock
    private lateinit var tagRepository: TagRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var bookmarkService: BookmarkService

    @Test
    internal fun `should get first page of all bookmarks if page number is not specified`() {
        given(bookmarkRepository.findAll(any<Pageable>())).willReturn(Page.empty())

        bookmarkService.getAllBookmarks()

        val pageableCaptor = argumentCaptor<PageRequest>()
        verify(bookmarkRepository).findAll(pageableCaptor.capture())
        assertThat(pageableCaptor.value.pageNumber).isEqualTo(0)
        assertThat(pageableCaptor.value.pageSize).isEqualTo(DEFAULT_PAGE_SIZE)
    }

    @Test
    internal fun `should get first page of user bookmarks if page number is not specified`() {
        val userId = 1L
        given(bookmarkRepository.findByCreatedById(anyLong(), any())).willReturn(Page.empty())

        bookmarkService.getBookmarksByUser(userId)

        val pageableCaptor = argumentCaptor<PageRequest>()
        verify(bookmarkRepository).findByCreatedById(anyLong(), capture(pageableCaptor))
        assertThat(pageableCaptor.value.pageNumber).isEqualTo(0)
        assertThat(pageableCaptor.value.pageSize).isEqualTo(DEFAULT_PAGE_SIZE)
    }

    @Test
    internal fun `show throw exception while getting bookmarks by tag if tag does not exist`() {
        given(tagRepository.findByName("non-existing-tag")).willReturn(Optional.empty())

        assertThatThrownBy { bookmarkService.getBookmarksByTag("non-existing-tag") }
            .isInstanceOf(ResourceNotFoundException::class.java)
    }
}
