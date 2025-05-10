package data.repositories.dataSource

import data.dto.TaskStateDto

interface TaskStateDataSource {
    suspend fun createTaskState(state: TaskStateDto): Boolean
    suspend fun editTaskState(editState: TaskStateDto): Boolean
    suspend fun deleteTaskState(stateId: String): Boolean
    suspend fun getTaskStates(): List<TaskStateDto>
}