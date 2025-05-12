package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskStateDto(
    @SerialName("_id")
    val _id: String,

    val title: String,

    @SerialName("project_id")
    val projectId: String,
)