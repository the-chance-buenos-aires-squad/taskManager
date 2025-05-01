package data.repositories.mappers

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime

class AuditMapper:Mapper<Audit>{
    override fun mapEntityToRow(entity: Audit): List<String> {
        return listOf(
            entity.id,
            entity.entityId,
            entity.entityType.name,
            entity.action.name,
            entity.field ?: "",
            entity.oldValue ?: "",
            entity.newValue ?: "",
            entity.userId,
            entity.timestamp.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Audit {
        return Audit(
            id = row[0],
            entityId = row[1],
            entityType = EntityType.entries.find { it.name == row[2]}!!,
            action = ActionType.valueOf(row[3]),
            field = row[4],
            oldValue = row[5],
            newValue = row[6],
            userId = row[7],
            timestamp = LocalDateTime.parse(row[8])
        )
    }


}