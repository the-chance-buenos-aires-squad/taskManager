package data.dummyData

import data.dto.AuditDto
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime
import java.util.*

object DummyAudits {
    val dummyTaskAudit_CreateAction = Audit(
        id = UUID.randomUUID(),
        entityId = "entity_id",
        entityType = EntityType.TASK,
        action = ActionType.CREATE,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = LocalDateTime.now()
    )

    val DummyTaskAuditDto = AuditDto(
        id = dummyTaskAudit_CreateAction.id.toString(),
        entityId = "entity_id",
        entityType = EntityType.TASK.name,
        action = ActionType.CREATE.name,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = dummyTaskAudit_CreateAction.timestamp.toString()
    )

    val DummyTaskAuditRow = listOf(
        dummyTaskAudit_CreateAction.id.toString(),
        dummyTaskAudit_CreateAction.entityId,
        dummyTaskAudit_CreateAction.entityType!!.name,
        dummyTaskAudit_CreateAction.action!!.name,
        dummyTaskAudit_CreateAction.field ?: "",
        dummyTaskAudit_CreateAction.oldValue ?: "",
        dummyTaskAudit_CreateAction.newValue ?: "",
        dummyTaskAudit_CreateAction.userId,
        dummyTaskAudit_CreateAction.timestamp.toString()
    )


    val dummyProjectAudit_CreateAction = Audit(
        id = UUID.randomUUID(),
        entityId = "entity_id",
        entityType = EntityType.PROJECT,
        action = ActionType.CREATE,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = LocalDateTime.now()
    )


}