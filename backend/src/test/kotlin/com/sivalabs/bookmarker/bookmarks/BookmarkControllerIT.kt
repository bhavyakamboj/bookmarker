package com.sivalabs.bookmarker.bookmarks

import com.sivalabs.bookmarker.domain.entity.Bookmark
import com.sivalabs.bookmarker.domain.entity.toDTO
import com.sivalabs.bookmarker.domain.model.BookmarkDTO
import com.sivalabs.bookmarker.domain.model.BookmarksResultDTO
import com.sivalabs.bookmarker.domain.model.TagDTO
import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.domain.repository.BookmarkRepository
import com.sivalabs.bookmarker.domain.repository.UserRepository
import com.sivalabs.bookmarker.domain.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

class BookmarkControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bookmarkRepository: BookmarkRepository

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        httpHeaders = null
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
    fun `should get first page bookmarks if page number is less than 1`() {
        val responseEntity = getAllBookmarks(-1)

        verifyStatusCode(responseEntity, OK)
        val bookmarksResults = responseEntity.body!!
        assertThat(bookmarksResults.content).isNotEmpty
        assertThat(bookmarksResults.currentPage).isEqualTo(1)
    }

    @Test
    fun `should get bookmark by id`() {
        val responseEntity = getBookmarkById(1)

        verifyStatusCode(responseEntity, OK)
        val bookmarkDTO = responseEntity.body!!
        assertThat(bookmarkDTO).isNotNull
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

        val bookmark =
            TestHelper.buildBookmark(null, "http://sivalabs.in", "SivaLabs Blog", "java", "spring", "newtag", " ")
        val responseEntity = createBookmark(bookmark.toDTO())

        verifyStatusCode(responseEntity, CREATED)
    }

    @Test
    fun `should create bookmark with title if not present`() {
        asAuthenticateUser(adminCredentials)

        val bookmark = TestHelper.buildBookmark(null, "http://sivalabs.in", "")
        val responseEntity = createBookmark(bookmark.toDTO())

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
    fun `deleting non-existing bookmark should return 404`() {
        val nonExistingBookmarkId = 9999L
        verifyBookmarkNotExists(nonExistingBookmarkId)

        asAuthenticateUser(adminCredentials)
        val response = deleteBookmark(nonExistingBookmarkId)

        verifyStatusCode(response, NOT_FOUND)
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

    private fun getAllBookmarks(pageNo: Int = 1): ResponseEntity<BookmarksResultDTO> {
        return restTemplate.getForEntity("/api/bookmarks?page=$pageNo", BookmarksResultDTO::class.java)
    }

    private fun getBookmarksByTag(tag: String): ResponseEntity<TagDTO> {
        return restTemplate.getForEntity("/api/bookmarks/tagged/$tag", TagDTO::class.java)
    }

    private fun getBookmarksByUser(userId: Long): ResponseEntity<BookmarksResultDTO> {
        return restTemplate.getForEntity("/api/bookmarks?userId=$userId", BookmarksResultDTO::class.java)
    }

    private fun createBookmark(bookmark: BookmarkDTO): ResponseEntity<BookmarkDTO> {
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
