package domain.usecases.audit

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import domain.repositories.AuditRepository
import java.time.LocalDateTime
import java.util.*

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {

    suspend fun execute(
        entityId: String,
        entityType: EntityType?,
        action: ActionType?,
        field: String?,
        oldValue: String?,
        newValue: String?,
        userId: String,
    ) {
        val id = UUID.randomUUID()
        val timeStamp = LocalDateTime.now()
        val newAudit = Audit(
            id = id,
            entityId = entityId,
            entityType = entityType,
            action = action,
            field = field,
            originalValue = oldValue,
            modifiedValue = newValue,
            userId = userId,
            timestamp = timeStamp
        )
        try {
            auditRepository.addAudit(newAudit)
        } catch (e: Exception) {
            println("Failed to add audit:${e.message}")
        }
    }

}