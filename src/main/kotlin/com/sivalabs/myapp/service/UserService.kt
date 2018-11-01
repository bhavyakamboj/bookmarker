package com.sivalabs.myapp.service

import com.sivalabs.myapp.config.Loggable
import com.sivalabs.myapp.entity.User
import com.sivalabs.myapp.model.GitHubRepoDTO
import com.sivalabs.myapp.model.UserDTO
import com.sivalabs.myapp.model.UserProfile
import com.sivalabs.myapp.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@Loggable
class UserService(private val userRepository: UserRepository, private val githubClient: GithubClient) {

    fun getAllUsers() : List<UserDTO> = userRepository.findAll().map { UserDTO.fromEntity(it) }


    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun createUser(user: UserDTO): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()))
    }

    fun updateUser(user: UserDTO): UserDTO {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()))
    }

    fun deleteUser(userId: Long) {
        userRepository.findById(userId).map { userRepository.delete(it) }
    }

    fun getUserProfile(id: Long): Optional<UserProfile> {
        val userOptional = this.getUserById(id)
        if (!userOptional.isPresent) {
            return Optional.empty()
        }
        val user = userOptional.get()
        val ghUserDTO = githubClient.getUser(user.githubUsername)
        ghUserDTO?.let {
            it.repos.sortedByDescending { repo: GitHubRepoDTO -> repo.stars  }
        }
        return Optional.of(UserProfile(user.id, user.name, user.email, ghUserDTO))
    }
}
