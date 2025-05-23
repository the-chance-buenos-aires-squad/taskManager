package data.dataSource.audit

import data.dto.AuditDto
import data.repositories.dataSource.AuditDataSource

class FakeMongoAuditDataSource : AuditDataSource {
    private val audits = mutableListOf<AuditDto>()
    override suspend fun addAudit(auditDto: AuditDto): Boolean {
        return audits.add(auditDto)
    }

    override suspend fun getAllAudit(): List<AuditDto> {
        return audits.toList()
    }
}