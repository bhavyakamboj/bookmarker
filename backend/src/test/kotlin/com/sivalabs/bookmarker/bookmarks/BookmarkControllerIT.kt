package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.bookmarks.entity.Bookmark
import com.sivalabs.bookmarker.bookmarks.model.BookmarkDTO
import com.sivalabs.bookmarker.bookmarks.model.BookmarksResultDTO
import com.sivalabs.bookmarker.bookmarks.model.TagDTO
import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.users.UserRepository
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK

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
        existingBookmark.createdBy = userRepository.findByEmail("siva@gmail.com").get()
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
        val responseEntity = restTemplate.exchange("/api/bookmarks", GET, null, BookmarksResultDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val bookmarksResults = responseEntity.body!!
        assertThat(bookmarksResults.content).isNotEmpty
    }

    @Test
    fun `should get bookmarks by tag`() {
        val responseEntity = restTemplate.exchange("/api/bookmarks/tagged/java", GET, null, TagDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val tagDTO = responseEntity.body!!
        assertThat(tagDTO).isNotNull
    }

    @Test
    fun `should get bookmarks by user`() {
        val responseEntity = restTemplate.exchange("/api/bookmarks?userId=${existingBookmark.createdBy.id}", GET, null, BookmarksResultDTO::class.java)
        val bookmarksResults = responseEntity.body!!
        assertThat(bookmarksResults.content).isNotEmpty
    }

    @Test
    fun `should create bookmark`() {
        val request = HttpEntity(newBookmark, getAuthHeaders())
        val responseEntity = restTemplate.postForEntity("/api/bookmarks", request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(CREATED)
    }

    @Test
    fun `should create bookmark with title if not present`() {
        newBookmark.url = "http://sivalabs.in"
        newBookmark.title = ""
        val request = HttpEntity(newBookmark, getAuthHeaders())
        val responseEntity = restTemplate.postForEntity("/api/bookmarks", request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(CREATED)
        assertThat(responseEntity.body?.title).isNotBlank()
    }

    @Test
    fun `should delete bookmark`() {
        val request = HttpEntity(null, getAuthHeaders("siva@gmail.com", "siva"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", DELETE, request, Void::class.java)
        assertThat(response.statusCode).isEqualTo(OK)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `should not be able to delete bookmark by other users`() {
        val request = HttpEntity(null, getAuthHeaders("prasad@gmail.com", "prasad"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", DELETE, request, Void::class.java)
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
    }

    @Test
    fun `admin should be able to delete bookmark of other users`() {
        var request = HttpEntity(null, getAuthHeaders("siva@gmail.com", "siva"))
        var responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        request = HttpEntity(null, getAuthHeaders("admin@gmail.com", "admin"))
        val response = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", DELETE, request, Void::class.java)
        assertThat(response.statusCode).isEqualTo(OK)
        responseEntity = restTemplate.exchange("/api/bookmarks/${existingBookmark.id}", GET, request, BookmarkDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }
}
