package data.dataSource.audit

import data.dto.AuditDto
import data.repositories.mappers.Mapper

class AuditDtoParser : Mapper<List<String>, AuditDto> {
    override fun fromType(type: List<String>): AuditDto {
        return AuditDto(
            id = type[ID_ROW],
            entityId = type[ENTITY_ID_ROW],
            entityType = type[ENTITY_TYPE_ROW],
            action = type[ACTION_ROW],
            field = type[FIELD_ROW],
            oldValue = type[OLD_VALUE_ROW],
            newValue = type[NEW_VALUE_ROW],
            userId = type[USER_ID_ROW],
            timestamp = type[TIME_STAMP_ROW],
        )
    }

    override fun toType(row: AuditDto): List<String> {
        return listOf(
            row.id,
            row.entityId,
            row.entityType ?: "",
            row.action ?: "",
            row.field ?: "",
            row.oldValue ?: "",
            row.newValue ?: "",
            row.userId,
            row.timestamp
        )
    }


    private companion object {
        private const val ID_ROW = 0
        private const val ENTITY_ID_ROW = 1
        private const val ENTITY_TYPE_ROW = 2
        private const val ACTION_ROW = 3
        private const val FIELD_ROW = 4
        private const val OLD_VALUE_ROW = 5
        private const val NEW_VALUE_ROW = 6
        private const val USER_ID_ROW = 7
        private const val TIME_STAMP_ROW = 8
    }
}