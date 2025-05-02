package data.dataSource.dummyData

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime


object createDummyAudits {
    val dummyTaskCreateAction = Audit(
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


    val dummyProjectCreateAction = Audit(
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

    val dummyUserUpdateAction = Audit(
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
