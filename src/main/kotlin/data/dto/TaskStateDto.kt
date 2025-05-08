package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class TaskStateDto(
    @BsonId
    val id: String,

    val name: String,

    @SerialName("project_id")
    val projectId: String,
)