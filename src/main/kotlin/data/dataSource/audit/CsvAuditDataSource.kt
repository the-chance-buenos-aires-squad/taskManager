package data.dataSource.audit

import data.dataSource.util.CsvHandler
import java.io.File


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
) : AuditDataSource {

    override suspend fun addAudit(auditRow: List<String>): Boolean {
        return try {
            csvHandler.write(
                row = auditRow,
                file = file,
                append = true
            )
            true
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }

    override suspend fun getAllAudit(): List<List<String>> {
        val rows = csvHandler.read(file)
        return rows
    }


}