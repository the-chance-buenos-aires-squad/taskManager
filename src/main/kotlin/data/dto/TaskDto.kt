package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class TaskDto(
    @SerialName("_id")
    val id: String,

    val title: String,

    val description: String,

    @SerialName("project_id")
    val projectId: String,

    @SerialName("state_id")
    val stateId: String,

    @SerialName("assigned_to")
    val assignedTo: String?,

    @SerialName("created_by")
    var createdBy: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)