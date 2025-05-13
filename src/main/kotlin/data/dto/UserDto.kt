package data.dto

import domain.entities.UserRole
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserDto(
    @BsonId
    val id: String,
    val username: String,
    val password: String = "",
    val role: UserRole?,
    val createdAt: String
)