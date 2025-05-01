package data.dataSource.dummyData

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime


object createDummyAudit {
    val dummyTaskAudit_CreateAction = Audit(
        id = "121",
        entityId = "entity_id",
        entityType = EntityType.TASK,
        action = ActionType.CREATE,
        field = null,
        oldValue = "old",
        newValue = "new",
        userId = "admin123",
        timestamp = LocalDateTime.now()
    )


    val dummyProjectAudit_CreateAction = Audit(
        id = "121",
        entityId = "entity_id",
        entityType = EntityType.PROJECT,
        action = ActionType.CREATE,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = LocalDateTime.now()
    )

    val dummyUserAudit_UpdateAction = Audit(
        id = "121",
        entityId = "entity_id",
        entityType = EntityType.USER,
        action = ActionType.UPDATE,
        field = "fields",
        oldValue = "old",
        newValue = "new",
        userId = "user_id",
        timestamp = LocalDateTime.now()
    )


}
