package data.dataSource.task

import data.dto.TaskDto
import data.repositories.mappers.Mapper

class TaskDtoParser : Mapper<List<String>, TaskDto> {

    override fun fromType(type: List<String>): TaskDto {
        return TaskDto(
            id = type[ID],
            title = type[TITLE],
            description = type[DESCRIPTION],
            projectId = type[PROJECT_ID],
            stateId = type[STATE_ID],
            assignedTo = type[ASSIGNED_TO].ifEmpty { null },
            createdBy = type[CREATED_BY],
            createdAt = type[CREATED_AT],
            updatedAt = type[UPDATED_AT]
        )
    }

    override fun toType(row: TaskDto): List<String> {
        return listOf(
            row.id,
            row.title,
            row.description,
            row.projectId,
            row.stateId,
            row.assignedTo ?: "",
            row.createdBy,
            row.createdAt,
            row.updatedAt
        )
    }

    companion object {
        const val ID = 0
        const val TITLE = 1
        const val DESCRIPTION = 2
        const val PROJECT_ID = 3
        const val STATE_ID = 4
        const val ASSIGNED_TO = 5
        const val CREATED_BY = 6
        const val CREATED_AT = 7
        const val UPDATED_AT = 8
    }
}