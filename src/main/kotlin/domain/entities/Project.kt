package domain.entities

import java.time.LocalDateTime
import java.util.UUID

data class Project(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val createdAt: LocalDateTime)