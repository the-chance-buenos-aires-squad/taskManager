package data.dataSource

import domain.entities.State

interface TaskStateDataSource {
    fun createTaskState(state: State): Boolean
    fun editTaskState(state: State): Boolean
    fun deleteTaskState(stateId: String): Boolean
    fun getAllTaskStates(): List<State>
    fun existsTaskState(stateId: String): Boolean
}