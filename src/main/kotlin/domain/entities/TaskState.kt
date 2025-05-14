package domain.entities

import java.util.*

data class TaskState(
    val id: UUID,
    val title: String,
    val projectId: UUID,
)