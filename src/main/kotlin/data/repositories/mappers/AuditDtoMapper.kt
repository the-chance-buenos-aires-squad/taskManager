package data.repositories.mappers

import data.dto.AuditDto
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime
import java.util.*

class AuditDtoMapper : Mapper<Audit, AuditDto> {

    override fun fromType(type: Audit): AuditDto {
        return AuditDto(
            id = type.id.toString(),
            entityId = type.entityId,
            entityType = type.entityType?.name,
            action = type.action?.name,
            field = type.field,
            oldValue = type.originalValue,
            newValue = type.modifiedValue,
            userId = type.userId,
            timestamp = type.timestamp.toString()
        )
    }

    override fun toType(row: AuditDto): Audit {
        return Audit(
            id = UUID.fromString(row.id),
            entityId = row.entityId,
            entityType = row.entityType?.let {
                runCatching { EntityType.valueOf(it) }.getOrNull()
            },
            action = row.action?.let {
                runCatching { ActionType.valueOf(it) }.getOrNull()
            },
            field = row.field,
            originalValue = row.oldValue,
            modifiedValue = row.newValue,
            userId = row.userId,
            timestamp = LocalDateTime.parse(row.timestamp)
        )
    }


}