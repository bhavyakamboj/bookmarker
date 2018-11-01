package com.sivalabs.myapp.model

import com.fasterxml.jackson.annotation.JsonProperty

class GitHubUserDTO {

    var id: Long? = null
    var name: String? = null
    var url: String? = null

    @JsonProperty("public_repos")
    var publicRepos: Int = 0

    var repos: List<GitHubRepoDTO>? = null
}
