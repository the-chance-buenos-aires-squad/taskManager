package data.dummyData

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime

object DummyAudits {
    val dummyTaskAudit_CreateAction = Audit(
        id = "1234",
        entityId = "entity_id",
        entityType = EntityType.TASK,
        action = ActionType.CREATE,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = LocalDateTime.now()
    )


    val dummyProjectAudit_CreateAction = Audit(
        id = "1234",
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