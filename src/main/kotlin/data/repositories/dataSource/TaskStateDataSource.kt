package data.repositories.dataSource

import domain.entities.TaskState
import java.util.*

interface TaskStateDataSource {
    fun createTaskState(state: List<String>): Boolean
    fun editTaskState(editState: List<String>): Boolean
    fun deleteTaskState(stateId: UUID): Boolean
    fun getAllTaskStates(): List<TaskState>
    fun existsTaskState(name: String, projectId: UUID): Boolean
}