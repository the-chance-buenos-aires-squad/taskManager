package domain.entities

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val password: String,
    val role: UserRole,
    val createdAt: LocalDateTime
)
