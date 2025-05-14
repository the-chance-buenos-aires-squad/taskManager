package domain.entities

import java.time.LocalDateTime
import java.util.*

data class Project(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val createdAt: LocalDateTime
)