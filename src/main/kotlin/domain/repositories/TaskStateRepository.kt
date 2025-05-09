package domain.repositories

import domain.entities.TaskState
import java.util.*

interface TaskStateRepository {
    suspend fun createTaskState(state: TaskState): Boolean
    suspend fun editTaskState(state: TaskState): Boolean
    suspend fun deleteTaskState(stateId: UUID): Boolean
    suspend fun getAllTaskStates(): List<TaskState>
}