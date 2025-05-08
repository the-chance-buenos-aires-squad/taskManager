package domain.usecases

import domain.entities.Audit
import domain.repositories.AuditRepository

class GetAllAuditUseCase(
    private val auditRepositoryImpl: AuditRepository
) {
    suspend fun getAllAudit(): List<Audit> {
        return auditRepositoryImpl.getAllAudit()
    }
}