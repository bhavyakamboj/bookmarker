package com.sivalabs.myapp.utils

import org.mockserver.client.server.MockServerClient
import org.mockserver.model.Header

import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.JsonBody.json

class MockEngine(private val mockServerClient: MockServerClient) {

    fun mockGetGithubUser(username: String) {
        val request = request("/users/$username")
        mockServerClient
                .`when`(request)
                .respond(
                        response()
                                .withHeader(Header.header("Content-Type", "application/json"))
                                .withBody(json(GET_GITHUB_USER_RESPONSE)))
    }

    fun mockGetGithubUserRepos(username: String) {
        val request = request("/users/$username/repos")
        mockServerClient
                .`when`(request)
                .respond(
                        response()
                                .withHeader(Header.header("Content-Type", "application/json"))
                                .withBody(json(GET_GITHUB_USER_REPOS_RESPONSE)))
    }

    companion object {

        private val mockResponsesDir = "/mockserver/responses"
        private val GET_GITHUB_USER_RESPONSE = mockResponse("/get-github-user-response.json")
        private val GET_GITHUB_USER_REPOS_RESPONSE = mockResponse("/get-github-user-repos-response.json")

        private fun mockResponse(file: String): String {
            return TestHelper.getClasspathResourceContent(mockResponsesDir + file)
        }
    }
}
