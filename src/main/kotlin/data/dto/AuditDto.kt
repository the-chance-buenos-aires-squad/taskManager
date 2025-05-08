package data.dto

import domain.entities.ActionType
import domain.entities.EntityType
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime

@Serializable
data class AuditDto(
    @BsonId
    val id: String,
    val entityId: String,
    val entityType: EntityType,
    val action: ActionType,
    val field: String?,
    val oldValue: String?,
    val newValue: String?,
    val userId: String,
    val timestamp: String
)
