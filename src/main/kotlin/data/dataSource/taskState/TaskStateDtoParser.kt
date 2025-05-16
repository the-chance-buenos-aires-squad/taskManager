package data.dataSource.taskState

import data.dto.TaskStateDto
import data.repositories.mappers.Mapper

class TaskStateDtoParser : Mapper<List<String>, TaskStateDto> {

    override fun fromType(type: List<String>): TaskStateDto {
        return TaskStateDto(
            _id = type[ID_ROW],
            name = type[TASK_STATE_NAME_ROW],
            projectId = type[PROJECT_ID_ROW]
        )
    }

    override fun toType(row: TaskStateDto): List<String> {
        return listOf(row._id, row.name, row.projectId)
    }

    companion object {
        private const val ID_ROW = 0
        private const val TASK_STATE_NAME_ROW = 1
        private const val PROJECT_ID_ROW = 2
    }

}
