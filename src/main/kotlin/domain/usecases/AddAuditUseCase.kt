package domain.usecases

import data.dataSource.CsvAuditDataSource
import data.repositories.AuditRepository
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {

    fun addAudit(
        entityId: String,
        entityType: EntityType,
        action: ActionType,
        field: String?,
        oldValue: String?,
        newValue: String?,
        userId: String,
    ):Audit{
        TODO()
    }

}