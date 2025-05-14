package data.repositories.mappers

import data.dto.TaskStateDto
import domain.entities.TaskState
import java.util.*

class TaskStateDtoMapper : Mapper<TaskState, TaskStateDto> {

    override fun fromEntity(entity: TaskState): TaskStateDto {
        return TaskStateDto(
            _id = entity.id.toString(),
            name = entity.title,
            projectId = entity.projectId.toString()
        )
    }

    override fun toEntity(type: TaskStateDto): TaskState {
        return TaskState(
            id = UUID.fromString(type._id),
            title = type.name,
            projectId = UUID.fromString(type.projectId)
        )
    }

}