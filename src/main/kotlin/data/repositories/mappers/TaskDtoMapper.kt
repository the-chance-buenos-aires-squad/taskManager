package data.repositories.mappers

import data.dto.TaskDto
import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class TaskDtoMapper : Mapper<Task, TaskDto> {

    override fun fromType(type: Task): TaskDto {
        return TaskDto(
            id = type.id.toString(),
            title = type.title,
            description = type.description,
            projectId = type.projectId.toString(),
            stateId = type.stateId.toString(),
            assignedTo = type.assignedTo?.toString(),
            createdBy = type.createdBy.toString(),
            createdAt = type.createdAt.toString(),
            updatedAt = type.updatedAt.toString()
        )
    }

    override fun toType(row: TaskDto): Task {
        return Task(
            id = UUID.fromString(row.id),
            title = row.title,
            description = row.description,
            projectId = UUID.fromString(row.projectId),
            stateId = UUID.fromString(row.stateId),
            assignedTo = row.assignedTo,
            createdBy = UUID.fromString(row.createdBy),
            createdAt = LocalDateTime.parse(row.createdAt),
            updatedAt = LocalDateTime.parse(row.updatedAt)
        )
    }
}