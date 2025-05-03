package domain.repositories

import domain.entities.TaskState

interface TaskStateRepository {
    fun createTaskState(state: TaskState): Boolean
    fun editTaskState(state: TaskState): Boolean
    fun deleteTaskState(stateId: String): Boolean
    fun getAllTaskStates(): List<TaskState>
    fun existsTaskState(stateId: String): Boolean
}