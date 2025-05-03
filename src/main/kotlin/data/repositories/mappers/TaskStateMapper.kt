package data.repositories.mappers

import domain.entities.TaskState

class TaskStateMapper : Mapper<TaskState> {
    override fun mapEntityToRow(entity: TaskState): List<String> {
        return listOf(
            entity.id,
            entity.name,
            entity.projectId
        )
    }

    override fun mapRowToEntity(row: List<String>): TaskState {
        return TaskState(
            id = row[ID],
            name = row[NAME],
            projectId = row[PROJECT_ID]
        )
    }

    companion object {
        const val ID = 0
        const val NAME = 1
        const val PROJECT_ID = 2
    }
}