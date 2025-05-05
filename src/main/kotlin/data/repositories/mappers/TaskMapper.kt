package data.repositories.mappers

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class TaskMapper : Mapper<Task> {

    override fun mapEntityToRow(entity: Task): List<String> {
        return listOf<String>(
            entity.id.toString(),
            entity.title,
            entity.description,
            entity.projectId.toString(),
            entity.stateId.toString(),
            entity.assignedTo.toString(),
            entity.createdBy.toString(),
            entity.createdAt.toString(),
            entity.updatedAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Task {
        return Task(
            id = UUID.fromString(row[0]),
            title = row[1],
            description = row[2],
            projectId = UUID.fromString(row[3]),
            stateId = UUID.fromString(row[4]),
            assignedTo = UUID.fromString(row[5].ifBlank { null }),
            createdBy = UUID.fromString(row[6]),
            createdAt = LocalDateTime.parse(row[7]),
            updatedAt = LocalDateTime.parse(row[8])
        )
    }
}
