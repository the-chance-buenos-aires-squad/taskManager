package data.dataSource.audit

interface AuditDataSource {
    suspend fun addAudit(auditRow: List<String>): Boolean
    suspend fun getAllAudit(): List<List<String>>
}