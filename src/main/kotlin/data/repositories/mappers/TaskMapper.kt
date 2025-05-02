package data.repositories.mappers

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class TaskMapper : Mapper<Task> {
    override fun mapEntityToRow(entity: Task): List<String> {
        return listOf(
            entity.id.toString(),
            entity.title,
            entity.description,
            entity.projectId,
            entity.stateId,
            entity.assignedTo.orEmpty(),
            entity.createdBy,
            entity.createdAt.toString(),
            entity.updatedAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Task {
        return Task(
            id = UUID.fromString(row[0]),
            title = row[1],
            description = row[2],
            projectId = row[3],
            stateId = row[4],
            assignedTo = row[5].ifBlank { null },
            createdBy = row[6],
            createdAt = LocalDateTime.parse(row[7]),
            updatedAt = LocalDateTime.parse(row[8])
        )
    }
}
