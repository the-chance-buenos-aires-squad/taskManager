package domain.repositories

import domain.entities.Audit

interface AuditRepository {
    fun addAudit(audit: Audit): Boolean
    fun getAllAudit(): List<Audit>
}