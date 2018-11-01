package com.sivalabs.myapp.service

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import com.sivalabs.myapp.config.Loggable
import com.sivalabs.myapp.model.GitHubRepoDTO
import com.sivalabs.myapp.model.GitHubUserDTO
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.Arrays

@Service
@Loggable
class GithubClient(private val restTemplate: RestTemplate) {

    private val log = LoggerFactory.getLogger(GithubClient::class.java)

    @Value("\${github.host}")
    private lateinit var githubApiBasePath: String

    @Timed("sb2s.githubclient.getuser")
    @HystrixCommand(fallbackMethod = "getDefaultUser",
                    commandProperties = [
                        HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
                    ])
    fun getUser(username: String): GitHubUserDTO? {
        log.info("process=get-github-user, username=$username")
        val userInfo = getUserInfo(username)
        userInfo?.let {
            it.repos = getUserRepos(username)
        }
        return userInfo
    }

    fun getDefaultUser(username: String): GitHubUserDTO {
        log.info("process=get-github-default-user, username=$username")
        return GitHubUserDTO(0,username,"",0, listOf())
    }

    private fun getUserInfo(username: String): GitHubUserDTO? {
        return restTemplate.getForObject("$githubApiBasePath/users/$username", GitHubUserDTO::class.java)
    }

    private fun getUserRepos(username: String): List<GitHubRepoDTO> {
        val repos = restTemplate.getForObject(
                "$githubApiBasePath/users/$username/repos", Array<GitHubRepoDTO>::class.java)
        return Arrays.asList(*repos!!)
    }
}
