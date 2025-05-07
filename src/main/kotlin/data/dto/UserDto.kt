package data.dto

import domain.entities.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val password: String,
    val role: UserRole?,
    val createdAt: String
)