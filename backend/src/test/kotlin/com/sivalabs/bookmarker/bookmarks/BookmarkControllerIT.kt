package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Bookmark
import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.users.UserRepository
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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

    @BeforeEach
    fun setUp() {
        newBookmark = TestHelper.buildBookmark()

        existingBookmark = TestHelper.buildBookmark()
        existingBookmark.createdBy = userRepository.findByEmail("siva@gmail.com")!!
        existingBookmark = bookmarkRepository.save(existingBookmark)
    }

    @AfterEach
    fun tearDown() {
        if (bookmarkRepository.existsById(newBookmark.id)) {
            bookmarkRepository.deleteById(newBookmark.id)
        }
        bookmarkRepository.deleteAll(bookmarkRepository.findAllById(listOf(existingBookmark.id)))
    }

    @Test
    fun `should get all bookmarks`() {
        val request = HttpEntity(null, getAuthHeaders())
        val responseEntity = restTemplate.exchange("/api/bookmarks", HttpMethod.GET, request, Array<BookmarkDTO>::class.java)
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
        val request = HttpEntity(null, getAuthHeaders("siva@gmail.com", "siva"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.DELETE, request,
        Void::class.java)
        assertThat(response.statusCode).isEqualTo(OK)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `should not be able to delete bookmark by other users`() {
        var request = HttpEntity(null, getAuthHeaders("prasad@gmail.com", "prasad"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.DELETE, request,
        Void::class.java)
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
    }

    @Test
    fun `admin should be able to delete bookmark of other users`() {
        var request = HttpEntity(null, getAuthHeaders("siva@gmail.com", "siva"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        request = HttpEntity(null, getAuthHeaders("admin@gmail.com", "admin"))
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.DELETE, request,
                Void::class.java)
        assertThat(response.statusCode).isEqualTo(OK)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", HttpMethod.GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }
}
