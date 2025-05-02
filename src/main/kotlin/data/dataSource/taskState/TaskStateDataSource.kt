package data.dataSource.taskState

import domain.entities.TaskState

interface TaskStateDataSource {
    fun createTaskState(state: List<String>): Boolean
    fun editTaskState(editState: List<String>): Boolean
    fun deleteTaskState(stateId: String): Boolean
    fun getAllTaskStates(): List<TaskState>
    fun existsTaskState(stateId: String): Boolean
}