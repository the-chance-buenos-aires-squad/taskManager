package data.repositories.dataSource

interface AuditDataSource {
    fun addAudit(auditRow: List<String>): Boolean
    fun getAllAudit(): List<List<String>>
}