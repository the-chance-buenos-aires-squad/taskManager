package data.dataSource.audit

import data.dataSource.util.CsvHandler
import data.dto.AuditDto
import data.repositories.dataSource.AuditDataSource
import java.io.File


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val auditDtoParser: AuditDtoParser,
    private val file: File
) : AuditDataSource {

    override suspend fun addAudit(auditDto: AuditDto): Boolean {
        csvHandler.write(
            row = auditDtoParser.fromDto(auditDto),
            file = file,
            append = true
        )
        return true // if no exception thrown.....
    }

    override suspend fun getAllAudit(): List<AuditDto> {
        val rows = csvHandler.read(file)
        return rows.map { auditDtoParser.toDto(it) }
    }


}