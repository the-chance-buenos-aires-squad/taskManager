package data.dto

import domain.entities.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("_id")
    val _id: String,
    val username: String,
    val password: String,
    val role: UserRole?,
    val createdAt: String
)