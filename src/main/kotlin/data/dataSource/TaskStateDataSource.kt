package data.dataSource

import domain.entities.TaskState

interface TaskStateDataSource {
    fun createTaskState(state: TaskState): Boolean
    fun editTaskState(editState: TaskState): Boolean
    fun deleteTaskState(stateId: String): Boolean
    fun getAllTaskStates(): List<TaskState>
    fun existsTaskState(stateId: String): Boolean
}