package domain.repositories

import domain.entities.Audit

interface AuditRepository {
    suspend fun addAudit(audit: Audit): Boolean
    suspend fun getAllAudit(): List<Audit>
}