package data.dataSource.dummyData

import data.repositories.mappers.AuditDtoMapper
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import java.time.LocalDateTime
import java.util.*


object createDummyAudits {
    val dummyTaskCreateAction = Audit(
        id = UUID.randomUUID(),
        entityId = "entity_id",
        entityType = EntityType.TASK,
        action = ActionType.CREATE,
        field = "",
        oldValue = "old",
        newValue = "new",
        userId = "admin123",
        timestamp = LocalDateTime.now()
    )
    val auditDtoMapper = AuditDtoMapper()
    val dummyTaskCreateActionRow = auditDtoMapper.fromEntity(dummyTaskCreateAction)


    val dummyProjectCreateAction = Audit(
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
    val dummyProjectCreateActionRow = auditDtoMapper.fromEntity(dummyProjectCreateAction)


    val dummyUserUpdateAction = Audit(
        id = UUID.randomUUID(),
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
