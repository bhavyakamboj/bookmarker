package com.sivalabs.myapp.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubUserDTO (
    var id: Long,
    var name: String,
    var url: String,
    @JsonProperty("public_repos")
    var publicRepos: Int = 0,
    var repos: List<GitHubRepoDTO> = listOf()
)
