package data.dataSource.auditDataSource

import data.dataSource.util.CsvHandler
import domain.entities.Audit
import java.io.File


class CsvAuditDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
):AuditDataSource {


    override fun addAudit(auditRow: List<String>): Boolean {
        return try {
            csvHandler.write(
                row = auditRow,
                file = file,
                append = true
            )
            true
        } catch (e: Exception) {

            false
        }
    }


    override fun getAllAudit(): List<String> {
        TODO()
    }




}