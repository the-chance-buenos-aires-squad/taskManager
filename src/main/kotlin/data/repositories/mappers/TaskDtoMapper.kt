package data.repositories.mappers

import data.dto.TaskDto
import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class TaskDtoMapper : Mapper<Task,TaskDto> {

    override fun fromEntity(entity: Task): TaskDto {
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

    override fun toEntity(type: TaskDto): Task {
        return Task(
            id = UUID.fromString(type.id),
            title = type.title,
            description = type.description,
            projectId = UUID.fromString(type.projectId),
            stateId = UUID.fromString(type.stateId),
            assignedTo = type.assignedTo?.let { UUID.fromString(it) },
            createdBy = UUID.fromString(type.createdBy),
            createdAt = LocalDateTime.parse(type.createdAt),
            updatedAt = LocalDateTime.parse(type.updatedAt)
        )
    }
}