package com.sivalabs.bookmarker.domain.repository

import com.sivalabs.bookmarker.domain.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
}
