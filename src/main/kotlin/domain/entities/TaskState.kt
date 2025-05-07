package domain.entities

import java.util.*

data class TaskState(
    val id: UUID,
    val name: String,
    val projectId: UUID,
)