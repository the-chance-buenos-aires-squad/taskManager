package data.repositories


import data.repositories.dataSource.AuditDataSource
import data.repositories.mappers.AuditDtoMapper
import domain.entities.Audit
import domain.repositories.AuditRepository

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource,
    private val auditDtoMapper: AuditDtoMapper
) : AuditRepository {

    override suspend fun addAudit(audit: Audit): Boolean {
        return auditDataSource.addAudit(auditDtoMapper.fromEntity(audit))
    }

    override suspend fun getAllAudit(): List<Audit> {
        val auditsRowList = auditDataSource.getAllAudit()
        return auditsRowList.map { row ->
            auditDtoMapper.toEntity(row)
        }
    }
}