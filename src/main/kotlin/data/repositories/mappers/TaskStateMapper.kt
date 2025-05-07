package data.repositories.mappers

import domain.entities.TaskState
import java.util.*

class TaskStateMapper : Mapper<TaskState,List<String>> {
     fun mapEntityToRow(entity: TaskState): List<String> {
        return listOf(
            entity.id.toString(),
            entity.name,
            entity.projectId.toString()
        )
    }

     fun mapRowToEntity(row: List<String>): TaskState {
        return TaskState(
            id = UUID.fromString(row[ID]),
            name = row[NAME],
            projectId = UUID.fromString(row[PROJECT_ID])
        )
    }

    companion object {
        const val ID = 0
        const val NAME = 1
        const val PROJECT_ID = 2
    }

    override fun fromEntity(entity: TaskState): List<String> {
        TODO("Not yet implemented")
    }

    override fun toEntity(type: List<String>): TaskState {
        TODO("Not yet implemented")
    }
}