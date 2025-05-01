package domain.entities

import java.time.LocalDateTime

data class Project(
    val id: String,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime
)