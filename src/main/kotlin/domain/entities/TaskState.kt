package domain.entities

import java.util.UUID

data class TaskState(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val projectId: UUID,
)