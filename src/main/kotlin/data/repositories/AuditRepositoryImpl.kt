package data.repositories

import data.dataSource.CsvAuditDataSource
import domain.entities.Audit
import domain.repositories.AuditRepository

class AuditRepositoryImpl(
    private val auditDataSource: CsvAuditDataSource
) :AuditRepository{

    override fun addAudit(audit: Audit): Boolean {
        TODO()
    }

    override fun getAllAudit(): List<Audit> {
        return auditDataSource.getAllAudit()
    }

}