package domain.entities

import java.time.LocalDateTime

data class Audit(
    val id: String,
    val entityId: String,
    val entityType: EntityType,
    val action: ActionType,
    val field: String?,
    val oldValue: String?,
    val newValue: String?,
    val userId: String,
    val timestamp: LocalDateTime
)