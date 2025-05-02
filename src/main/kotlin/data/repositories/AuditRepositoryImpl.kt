package data.repositories


import data.dataSource.auditDataSource.AuditDataSource
import data.repositories.mappers.Mapper
import domain.entities.Audit
import domain.repositories.AuditRepository

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource,
    private val auditMapper: Mapper<Audit>
) : AuditRepository {

    override fun addAudit(audit: Audit): Boolean {
        return auditDataSource.addAudit(auditMapper.mapEntityToRow(audit))
    }

    override fun getAllAudit(): List<Audit> {
        val auditsRowList = auditDataSource.getAllAudit()
        return auditsRowList.map { row ->
            auditMapper.mapRowToEntity(row)
        }
    }


}