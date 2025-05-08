package data.dataSource.taskState

import data.dto.TaskStateDto
import domain.entities.TaskState
import java.util.*

interface TaskStateDataSource {
    suspend fun createTaskState(state: TaskStateDto): Boolean
    suspend fun editTaskState(editState: TaskStateDto): Boolean
    suspend fun deleteTaskState(stateId: String): Boolean
    suspend fun getTaskStates(): List<TaskStateDto>
    suspend fun existsTaskState(name: String, projectId: String): Boolean
}