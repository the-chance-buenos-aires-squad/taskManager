package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
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
    val createdBy: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)