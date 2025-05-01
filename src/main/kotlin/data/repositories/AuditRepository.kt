package data.repositories

import data.dataSource.CsvAuditDataSource
import domain.entities.Audit

class AuditRepository(
    private val auditDataSource: CsvAuditDataSource
) {

    fun addAudit(audit:Audit):Boolean{
       return auditDataSource.addAudit(audit)
    }


    fun getAllAudit():List<Audit>{
        TODO()
    }




}