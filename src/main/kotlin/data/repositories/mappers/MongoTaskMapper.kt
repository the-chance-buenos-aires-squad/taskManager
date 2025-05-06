package data.repositories.mappers

import data.dto.TaskDto
import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class MongoTaskMapper : Mapper<Task,TaskDto> {
    
    override fun toMap(entity: Task): TaskDto {
        return TaskDto(id = entity.id.toString(),
            title = entity.title,
            description = entity.description,
            projectId = entity.projectId.toString(),
            stateId = entity.stateId.toString(),
            assignedTo = entity.assignedTo?.toString(),
            createdBy = entity.createdBy.toString(),
            createdAt = entity.createdAt.toString(),
            updatedAt = entity.updatedAt.toString()
        )
    }

    override fun fromMap(row: TaskDto): Task {
        return Task(
            id = UUID.fromString(row.id),
            title = row.title,
            description = row.description,
            projectId = UUID.fromString(row.projectId),
            stateId = UUID.fromString(row.stateId),
            assignedTo = row.assignedTo?.let { UUID.fromString(it) },
            createdBy = UUID.fromString(row.createdBy),
            createdAt = LocalDateTime.parse(row.createdAt),
            updatedAt = LocalDateTime.parse(row.updatedAt)
        )
    }
}