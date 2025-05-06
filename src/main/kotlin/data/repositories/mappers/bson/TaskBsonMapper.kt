package data.repositories.mappers.bson

import domain.entities.Task
import org.bson.Document
import java.time.LocalDateTime
import java.util.*

class TaskBsonMapper:BsonMapper<Task> {
    override fun toDocument(entity: Task): Document {
        return Document()
            .append("id", entity.id.toString())
            .append("title", entity.title)
            .append("description", entity.description)
            .append("projectId", entity.projectId.toString())
            .append("stateId", entity.stateId.toString())
            .append("assignedTo", entity.assignedTo?.toString())
            .append("createdBy", entity.createdBy.toString())
            .append("createdAt", entity.createdAt.toString())
            .append("updatedAt", entity.updatedAt.toString())
    }

    override fun fromDocument(document: Document): Task {
        return Task(
            id = UUID.fromString(document.getString("id")),
            title = document.getString("title"),
            description = document.getString("description"),
            projectId = UUID.fromString(document.getString("projectId")),
            stateId = UUID.fromString(document.getString("stateId")),
            assignedTo = document.getString("assignedTo")?.let { UUID.fromString(it) },
            createdBy = UUID.fromString(document.getString("createdBy")),
            createdAt = LocalDateTime.parse(document.getString("createdAt")),
            updatedAt = LocalDateTime.parse(document.getString("updatedAt"))
        )
    }
}