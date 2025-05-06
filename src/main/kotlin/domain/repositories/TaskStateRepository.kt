package domain.repositories

import domain.entities.TaskState
import java.util.UUID

interface TaskStateRepository {
    fun createTaskState(state: TaskState): Boolean
    fun editTaskState(state: TaskState): Boolean
    fun deleteTaskState(stateId: UUID,projectId: UUID): Boolean
    fun getAllTaskStates(): List<TaskState>
    fun existsTaskState(stateId: UUID): Boolean
}