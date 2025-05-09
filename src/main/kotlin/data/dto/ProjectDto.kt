package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    @SerialName("_id")
    val _id: String,
    val name: String,
    val description: String,
    @SerialName("created_at")
    val createdAt: String
)