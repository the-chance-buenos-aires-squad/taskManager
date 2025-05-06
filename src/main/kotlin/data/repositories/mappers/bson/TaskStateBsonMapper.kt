package data.repositories.mappers.bson

import domain.entities.TaskState
import org.bson.Document
import java.util.*

class TaskStateBsonMapper:BsonMapper<TaskState> {
    override fun toDocument(entity: TaskState): Document {
        return Document()
            .append("id", entity.id)
            .append("name", entity.name)
            .append("projectId", entity.projectId)
    }

    override fun fromDocument(document: Document): TaskState {
        return TaskState(
            id = UUID.fromString(document.getString("id")).toString(),
            name = document.getString("name"),
            projectId = UUID.fromString(document.getString("projectId")).toString()
        )
    }
}