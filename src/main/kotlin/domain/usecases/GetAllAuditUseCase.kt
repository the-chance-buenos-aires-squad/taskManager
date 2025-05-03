package domain.usecases

import data.repositories.AuditRepositoryImpl
import domain.entities.Audit
import domain.repositories.AuditRepository

class GetAllAuditUseCase(
    private val auditRepositoryImpl: AuditRepository
) {
    fun getAllAudit(): List<Audit> {
        return auditRepositoryImpl.getAllAudit()
    }
}