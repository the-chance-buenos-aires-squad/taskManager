package domain.entities

import java.time.LocalDateTime

data class User(
    val id: String,
    val username: String,
    val password: String,
    val role: UserRole,
    val createdAt: LocalDateTime
)
