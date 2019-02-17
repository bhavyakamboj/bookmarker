package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.users.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
}
