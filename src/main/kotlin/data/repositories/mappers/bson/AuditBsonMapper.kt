package data.repositories.mappers.bson

import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import org.bson.Document
import java.time.LocalDateTime

class AuditBsonMapper:BsonMapper<Audit> {
    override fun toDocument(entity: Audit): Document {
        return Document()
            .append("id", entity.id)
            .append("entityId", entity.entityId)
            .append("entityType", entity.entityType.name)
            .append("action", entity.action.name)
            .append("field", entity.field)
            .append("oldValue", entity.oldValue)
            .append("newValue", entity.newValue)
            .append("userId", entity.userId)
            .append("timestamp", entity.timestamp.toString())
    }

    override fun fromDocument(document: Document): Audit {
        return Audit(
            id = document.getString("id"),
            entityId = document.getString("entityId"),
            entityType = EntityType.valueOf(document.getString("entityType")),
            action = ActionType.valueOf(document.getString("action")),
            field = document.getString("field"),
            oldValue = document.getString("oldValue"),
            newValue = document.getString("newValue"),
            userId = document.getString("userId"),
            timestamp = LocalDateTime.parse(document.getString("timestamp"))
        )
    }
}