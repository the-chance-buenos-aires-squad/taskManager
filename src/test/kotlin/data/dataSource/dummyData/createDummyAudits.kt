package data.dataSource.dummyData

import data.repositories.mappers.AuditMapper
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
        field = "",
        oldValue = "old",
        newValue = "new",
        userId = "admin123",
        timestamp = LocalDateTime.now()
    )
    val auditMapper = AuditMapper()
    val dummyTaskCreateActionRow = auditMapper.mapEntityToRow(dummyTaskCreateAction)


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
    val dummyProjectCreateActionRow = auditMapper.mapEntityToRow(dummyProjectCreateAction)


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
