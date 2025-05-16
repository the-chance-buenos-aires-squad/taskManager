package data.repositories.mappers

import data.dto.TaskStateDto
import domain.entities.TaskState
import java.util.*

class TaskStateDtoMapper : Mapper<TaskState, TaskStateDto> {

    override fun fromType(type: TaskState): TaskStateDto {
        return TaskStateDto(
            _id = type.id.toString(),
            name = type.title,
            projectId = type.projectId.toString()
        )
    }

    override fun toType(row: TaskStateDto): TaskState {
        return TaskState(
            id = UUID.fromString(row._id),
            title = row.name,
            projectId = UUID.fromString(row.projectId)
        )
    }

}