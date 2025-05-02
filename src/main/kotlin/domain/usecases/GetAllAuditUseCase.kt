package domain.usecases

import data.repositories.AuditRepositoryImpl
import domain.entities.Audit

class GetAllAuditUseCase(
    private val auditRepositoryImpl: AuditRepositoryImpl
) {
    fun getAllAudit(): List<Audit> {
        return auditRepositoryImpl.getAllAudit()
    }
}