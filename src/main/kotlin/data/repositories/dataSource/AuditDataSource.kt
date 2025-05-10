package data.repositories.dataSource

import data.dto.AuditDto

interface AuditDataSource {
    suspend fun addAudit(auditDto: AuditDto): Boolean
    suspend fun getAllAudit(): List<AuditDto>
}