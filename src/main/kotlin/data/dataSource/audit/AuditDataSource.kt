package data.dataSource.audit

interface AuditDataSource {
    fun addAudit(auditRow: List<String>): Boolean
    fun getAllAudit(): List<List<String>>
}