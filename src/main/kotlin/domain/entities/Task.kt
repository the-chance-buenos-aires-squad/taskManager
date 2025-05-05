package domain.entities

import java.time.LocalDateTime
import java.util.*

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val projectId: UUID,
    val stateId: String,
    val assignedTo: UUID?,
    val createdBy: UUID,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now())