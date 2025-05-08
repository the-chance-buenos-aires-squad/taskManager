package data.dataSource.audit

import data.dataSource.DtoParser
import data.dto.AuditDto

class AuditDtoParser: DtoParser<List<String>, AuditDto> {
    override fun toDto(type: List<String>): AuditDto {
        TODO("Not yet implemented")
    }

    override fun fromDto(dto: AuditDto): List<String> {
        TODO("Not yet implemented")
    }
}