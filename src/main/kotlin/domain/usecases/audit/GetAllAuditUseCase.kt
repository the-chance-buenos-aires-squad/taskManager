package domain.usecases.audit

import domain.entities.Audit
import domain.repositories.AuditRepository

class GetAllAuditUseCase(
    private val auditRepositoryImpl: AuditRepository
) {
    suspend fun execute(): List<Audit> {
        return auditRepositoryImpl.getAllAudit()
    }
}