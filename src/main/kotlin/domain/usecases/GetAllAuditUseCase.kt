package domain.usecases

import data.repositories.AuditRepository
import domain.entities.Audit

class GetAllAuditUseCase(private val auditRepository: AuditRepository) {
    fun getAllAudit():List<Audit>{
        return auditRepository.getAllAudit()
    }

}