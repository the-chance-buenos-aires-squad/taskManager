package domain.usecases

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import domain.repositories.AuditRepository
import java.time.LocalDateTime
import java.util.*

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {

    suspend fun addAudit(
        entityId: String,
        entityType: EntityType,
        action: ActionType,
        field: String?,
        oldValue: String?,
        newValue: String?,
        userId: String,
    ): Audit? {
        val id = UUID.randomUUID()
        val timeStamp = LocalDateTime.now()
        val newAudit = Audit(
            id = id,
            entityId = entityId,
            entityType = entityType,
            action = action,
            field = field,
            oldValue = oldValue,
            newValue = newValue,
            userId = userId,
            timestamp = timeStamp
        )
        val result = auditRepository.addAudit(newAudit)

        return when (result) {
            true -> newAudit
            false -> null
        }

    }

}