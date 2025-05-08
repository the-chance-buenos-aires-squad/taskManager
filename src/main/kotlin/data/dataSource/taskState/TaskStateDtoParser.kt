package data.dataSource.taskState

import data.dataSource.DtoParser
import data.dto.TaskStateDto

class TaskStateDtoParser : DtoParser<List<String>, TaskStateDto> {

    override fun toDto(type: List<String>): TaskStateDto {
        return TaskStateDto(
            id = type[ID_ROW],
            name = type[TASK_STATE_NAME_ROW],
            projectId = type[PROJECT_ID_ROW]
        )
    }

    override fun fromDto(dto: TaskStateDto): List<String> {
        return listOf(dto.id, dto.name, dto.projectId)
    }

    companion object {
        private const val ID_ROW = 0
        private const val TASK_STATE_NAME_ROW = 1
        private const val PROJECT_ID_ROW = 2
    }

}
