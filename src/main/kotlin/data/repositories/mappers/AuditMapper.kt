package data.repositories.mappers

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime

class AuditMapper : Mapper<Audit,List<String>> {
     fun mapEntityToRow(entity: Audit): List<String> {
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

     fun mapRowToEntity(row: List<String>): Audit {
        return Audit(
            id = row[ID_ROW],
            entityId = row[ENTITY_IT],
            entityType = EntityType.entries.find { it.name == row[ENTITY_TYPE_ROW] }!!,
            action = ActionType.valueOf(row[ACTION_ROW]),
            field = row[FIELD_ROW],
            oldValue = row[OLD_VALUE_ROW],
            newValue = row[NEW_VALUE_ROW],
            userId = row[USER_ID_ROW],
            timestamp = LocalDateTime.parse(row[TIMESTAMP_ROW])
        )
    }

    companion object {
        const val ID_ROW = 0
        const val ENTITY_IT = 1
        const val ENTITY_TYPE_ROW = 2
        const val ACTION_ROW = 3
        const val FIELD_ROW = 4
        const val OLD_VALUE_ROW = 5
        const val NEW_VALUE_ROW = 6
        const val USER_ID_ROW = 7
        const val TIMESTAMP_ROW = 8
    }

    override fun fromEntity(entity: Audit): List<String> {
        TODO("Not yet implemented")
    }

    override fun toEntity(type: List<String>): Audit {
        TODO("Not yet implemented")
    }


}