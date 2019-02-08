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
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

class BookmarkControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bookmarkRepository: BookmarkRepository

    private var httpHeaders: HttpHeaders? = null

    private val adminCredentials = Pair("admin@gmail.com", "admin")
    private val user1Credentials = Pair("siva@gmail.com", "siva")
    private val user2Credentials = Pair("prasad@gmail.com", "prasad")

    private val adminUserId = 1L

    @BeforeEach
    fun setUp() {
        httpHeaders = null
    }

    @AfterEach
    fun tearDown() {
    }

    private fun createBookmarkByUser(userEmail: String): Bookmark {
        var bookmark = TestHelper.buildBookmark()
        bookmark.createdBy = userRepository.findByEmail(userEmail).get()
        return bookmarkRepository.save(bookmark)
    }

    @Test
    fun `should get all bookmarks`() {
        val responseEntity = getAllBookmarks()

        verifyStatusCode(responseEntity, OK)
        val bookmarksResults = responseEntity.body!!
        assertThat(bookmarksResults.content).isNotEmpty
    }

    @Test
    fun `should get bookmarks by tag`() {
        val responseEntity = getBookmarksByTag("java")

        verifyStatusCode(responseEntity, OK)
        val tagDTO = responseEntity.body!!
        assertThat(tagDTO).isNotNull
    }

    @Test
    fun `should get bookmarks by user`() {
        val responseEntity = getBookmarksByUser(adminUserId)

        verifyStatusCode(responseEntity, OK)
        val bookmarksResults = responseEntity.body!!
        assertThat(bookmarksResults.content).isNotEmpty
    }


    @Test
    fun `should create bookmark`() {
        asAuthenticateUser(user1Credentials)

        val bookmark = TestHelper.buildBookmark(null, "http://sivalabs.in", "SivaLabs Blog")
        val responseEntity = createBookmark(bookmark)

        verifyStatusCode(responseEntity, CREATED)
    }


    @Test
    fun `should create bookmark with title if not present`() {
        asAuthenticateUser(adminCredentials)

        val bookmark = TestHelper.buildBookmark(null, "http://sivalabs.in", "")
        val responseEntity = createBookmark(bookmark)

        verifyStatusCode(responseEntity, CREATED)
        assertThat(responseEntity.body?.title).isNotBlank()
    }

    @Test
    fun `should delete bookmark`() {
        val bookmark = createBookmarkByUser(user1Credentials.first)
        verifyBookmarkExists(bookmark.id)

        asAuthenticateUser(user1Credentials)
        val response = deleteBookmark(bookmark.id)

        verifyStatusCode(response, OK)

        verifyBookmarkNotExists(bookmark.id)
    }

    @Test
    fun `should not be able to delete bookmark by other users`() {
        val bookmark = createBookmarkByUser(user1Credentials.first)
        verifyBookmarkExists(bookmark.id)

        asAuthenticateUser(user2Credentials)
        val response = deleteBookmark(bookmark.id)

        verifyStatusCode(response, NOT_FOUND)

        verifyBookmarkExists(bookmark.id)
    }

    @Test
    fun `admin should be able to delete bookmark of other users`() {
        val bookmark = createBookmarkByUser(user1Credentials.first)
        verifyBookmarkExists(bookmark.id)

        asAuthenticateUser(adminCredentials)
        val response = deleteBookmark(bookmark.id)

        verifyStatusCode(response, OK)

        verifyBookmarkNotExists(bookmark.id)
    }

    private fun asAuthenticateUser(credentials: Pair<String, String>) {
        httpHeaders = getAuthHeaders(credentials.first, credentials.second)
    }

    private fun verifyStatusCode(responseEntity: ResponseEntity<*>, code: HttpStatus) {
        assertThat(responseEntity.statusCode).isEqualTo(code)
    }

    private fun getAllBookmarks(): ResponseEntity<BookmarksResultDTO> {
        return restTemplate.getForEntity("/api/bookmarks", BookmarksResultDTO::class.java)
    }

    private fun getBookmarksByTag(tag: String): ResponseEntity<TagDTO> {
        return restTemplate.getForEntity("/api/bookmarks/tagged/$tag", TagDTO::class.java)
    }

    private fun getBookmarksByUser(userId: Long): ResponseEntity<BookmarksResultDTO> {
        return restTemplate.getForEntity("/api/bookmarks?userId=$userId", BookmarksResultDTO::class.java)
    }

    private fun createBookmark(bookmark: Bookmark): ResponseEntity<BookmarkDTO> {
        val request = HttpEntity(bookmark, httpHeaders)
        return restTemplate.postForEntity("/api/bookmarks", request, BookmarkDTO::class.java)
    }

    private fun getBookmarkById(id: Long): ResponseEntity<BookmarkDTO> {
        return restTemplate.getForEntity("/api/bookmarks/$id", BookmarkDTO::class.java)
    }

    private fun deleteBookmark(id: Long): ResponseEntity<Void> {
        val request = HttpEntity(null, httpHeaders)
        return restTemplate.exchange("/api/bookmarks/$id", DELETE, request, Void::class.java)
    }

    private fun verifyBookmarkExists(id: Long) {
        val responseEntity = getBookmarkById(id)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
    }

    private fun verifyBookmarkNotExists(id: Long) {
        val responseEntity = getBookmarkById(id)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }
}
