package com.sivalabs.myapp.model

import com.fasterxml.jackson.annotation.JsonProperty

class GitHubRepoDTO {

    var id: Long = 0
    var name: String? = null
    var description: String? = null

    @JsonProperty("html_url")
    var url: String? = null

    var language: String? = null
    var forks: Int = 0

    @JsonProperty("stargazers_count")
    var stars: Int = 0
}
