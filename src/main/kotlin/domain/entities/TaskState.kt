package domain.entities

import java.util.*

data class TaskState(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val projectId: String,
)