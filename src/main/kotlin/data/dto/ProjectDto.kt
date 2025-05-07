package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class ProjectDto(
    @BsonId
    val id: String,
    val name: String,
    val description: String,
    @SerialName("created_at")
    val createdAt: String
)