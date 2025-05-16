package domain.entities

import java.time.LocalDateTime
import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val projectId: UUID,
    val stateId: UUID,
    val assignedTo: UUID?,
    val createdBy: UUID?=null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)