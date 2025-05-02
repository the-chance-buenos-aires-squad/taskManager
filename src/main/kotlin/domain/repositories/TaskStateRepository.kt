package domain.repositories

import domain.entities.State

interface TaskStateRepository {
    fun createTaskState(state: State): Boolean
    fun editTaskState(state: State): Boolean
    fun deleteTaskState(stateId: String): Boolean
    fun getAllTaskStates(): List<State>
    fun existsTaskState(stateId: String): Boolean
}