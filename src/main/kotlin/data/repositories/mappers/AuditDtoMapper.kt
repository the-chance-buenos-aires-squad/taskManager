package data.repositories.mappers

import data.dto.AuditDto
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime
import java.util.*

class AuditDtoMapper : Mapper<Audit, AuditDto> {

    override fun fromEntity(entity: Audit): AuditDto {
        return AuditDto(
            id = entity.id.toString(),
            entityId = entity.entityId,
            entityType = entity.entityType?.name,
            action = entity.action?.name,
            field = entity.field,
            oldValue = entity.oldValue,
            newValue = entity.newValue,
            userId = entity.userId,
            timestamp = entity.timestamp.toString()
        )
    }

    override fun toEntity(type: AuditDto): Audit {
        return Audit(
            id = UUID.fromString(type.id),
            entityId = type.entityId,
            entityType = type.entityType?.let {
                runCatching { EntityType.valueOf(it) }.getOrNull()
            },
            action = type.action?.let {
                runCatching { ActionType.valueOf(it) }.getOrNull()
            },
            field = type.field,
            oldValue = type.oldValue,
            newValue = type.newValue,
            userId = type.userId,
            timestamp = LocalDateTime.parse(type.timestamp)
        )
    }


}