package data.repositories.mappers

import data.dto.AuditDto
import domain.entities.Audit
import java.time.LocalDateTime
import java.util.*

class AuditDtoMapper : Mapper<Audit, AuditDto> {

    override fun fromEntity(entity: Audit): AuditDto {
        return AuditDto(
            id = entity.id.toString(),
            entityId = entity.entityId,
            entityType = entity.entityType,
            action = entity.action,
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
            entityType = type.entityType,
            action = type.action,
            field = type.field,
            oldValue = type.oldValue,
            newValue = type.newValue,
            userId = type.userId,
            timestamp = LocalDateTime.parse(type.timestamp)
        )
    }


}