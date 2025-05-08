package data.dataSource.audit

import data.dataSource.util.CsvHandler
import data.dto.AuditDto
import java.io.File


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val auditDtoParser: AuditDtoParser,
    private val file: File
) : AuditDataSource {

    override suspend fun addAudit(auditDto: AuditDto): Boolean {
        return try {
            csvHandler.write(
                row = auditDtoParser.fromDto(auditDto),
                file = file,
                append = true
            )
            true
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }

    override suspend fun getAllAudit(): List<AuditDto> {
        val rows = csvHandler.read(file)
        return rows.map { auditDtoParser.toDto(it) }
    }


}