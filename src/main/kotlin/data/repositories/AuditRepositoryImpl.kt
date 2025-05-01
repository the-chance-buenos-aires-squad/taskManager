package data.repositories

import data.dataSource.auditDataSource.AuditDataSource
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.repositories.mappers.AuditMapper
import domain.entities.Audit
import domain.repositories.AuditRepository

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource,
    private val auditMapper: AuditMapper
) : AuditRepository {

    override fun addAudit(audit:Audit):Boolean{
       return auditDataSource.addAudit(auditMapper.mapEntityToRow(audit))
    }


    override fun getAllAudit():List<Audit>{
        TODO()
    }




}