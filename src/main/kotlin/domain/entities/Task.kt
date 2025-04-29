package domain.entities

import java.time.LocalDateTime
import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val projectId: String,
    val stateId: String,
    val assignedTo: String?,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)