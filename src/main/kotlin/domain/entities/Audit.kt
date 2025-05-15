package domain.entities

import java.time.LocalDateTime
import java.util.*

data class Audit(
    val id: UUID = UUID.randomUUID(),
    val entityId: String,
    val entityType: EntityType?,
    val action: ActionType?,
    val field: String?,
    val originalValue: String?,
    val modifiedValue: String?,
    val userId: String,
    val timestamp: LocalDateTime
)