package data.repositories

import data.dataSource.CsvAuditDataSource

class AuditRepository(
    private val auditDataSource: CsvAuditDataSource
) {
}