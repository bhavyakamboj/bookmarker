package com.sivalabs.myapp.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubRepoDTO(
    var id: Long = 0,
    var name: String,
    var description: String?,
    @JsonProperty("html_url")
    var url: String,
    var language: String?,
    var forks: Int = 0,
    @JsonProperty("stargazers_count")
    var stars: Int = 0
)
