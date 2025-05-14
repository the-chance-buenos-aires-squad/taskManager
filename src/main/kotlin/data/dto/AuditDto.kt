package data.dto

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class AuditDto(
    @BsonId
    val id: String,
    val entityId: String,
    val entityType: String?,
    val action: String?,
    val field: String?,
    val oldValue: String?,
    val newValue: String?,
    val userId: String,
    val timestamp: String
)
