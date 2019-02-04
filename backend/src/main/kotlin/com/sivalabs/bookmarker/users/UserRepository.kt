package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.users.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
}
