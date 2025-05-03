package data.dataSource.auditDataSource

import data.dataSource.util.CsvHandler
import java.io.File


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
) : AuditDataSource {



    override fun addAudit(auditRow: List<String>): Boolean {
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


    override fun getAllAudit(): List<List<String>> {
        val rows = csvHandler.read(file)
        return rows
    }


}