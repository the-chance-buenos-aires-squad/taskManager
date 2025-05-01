package data.dataSource.auditDataSource

interface AuditDataSource {
    fun addAudit(auditRow: List<String>): Boolean
    fun getAllAudit(): List<String>
}