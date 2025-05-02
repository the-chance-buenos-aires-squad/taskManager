package data.mapper

import domain.entities.TaskState
import domain.entities.TaskStateIndices

object TaskStateMapper {
    fun map(parts: List<String>): TaskState? {
        return if (parts.size == 3) {
            TaskState(
                id = parts[TaskStateIndices.ID],
                name = parts[TaskStateIndices.NAME],
                projectId = parts[TaskStateIndices.PROJECT_ID]
            )
        } else {
            null
        }
    }
}