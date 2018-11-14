package com.sivalabs.bookmarker.controller

import com.sivalabs.bookmarker.entity.Bookmark
import com.sivalabs.bookmarker.model.BookmarkDTO
import com.sivalabs.bookmarker.repo.BookmarkRepository
import com.sivalabs.bookmarker.repo.UserRepository
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import java.util.Arrays.asList

class BookmarkControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bookmarkRepository: BookmarkRepository

    lateinit var existingBookmark: Bookmark
    lateinit var newBookmark: Bookmark

    @Before
    fun setUp() {
        newBookmark = TestHelper.buildBookmark()

        existingBookmark = TestHelper.buildBookmark()
        existingBookmark.createdBy = userRepository.findByEmail("admin@gmail.com")!!
        existingBookmark = bookmarkRepository.save(existingBookmark)
    }

    @After
    fun tearDown() {
        if (bookmarkRepository.existsById(newBookmark.id)) {
            bookmarkRepository.deleteById(newBookmark.id)
        }
        bookmarkRepository.deleteAll(bookmarkRepository.findAllById(listOf(existingBookmark.id)))
    }

    @Test
    fun `should get all bookmarks`() {
        val responseEntity = restTemplate.getForEntity("/api/bookmarks", Array<BookmarkDTO>::class.java)
        val bookmarks = asList(*responseEntity.body!!)
        assertThat(bookmarks).isNotEmpty
    }

    @Test
    fun `should create bookmark`() {
        val request = HttpEntity(newBookmark, getAuthHeaders())
        val responseEntity = restTemplate.postForEntity("/api/bookmarks", request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(CREATED)
    }

    @Test
    fun `should delete bookmark`() {
        val request = HttpEntity(newBookmark, getAuthHeaders())
        var responseEntity = restTemplate.getForEntity("/api/bookmarks/${existingBookmark.id}", BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.DELETE, request, Void::class.java)
        responseEntity = restTemplate.getForEntity("/api/bookmarks/${existingBookmark.id}", BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }
}
