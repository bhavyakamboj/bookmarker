package com.sivalabs.myapp.model

data class UserProfile(
    var id: Long,
    var name: String,
    var email: String,
    var githubProfile: GitHubUserDTO?
)
