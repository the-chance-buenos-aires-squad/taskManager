package domain.repositories

import domain.entities.State

interface TaskStateRepository {
    fun createState(state: State): Boolean
    fun editState(state: State): Boolean
    fun deleteState(stateId: String): Boolean
    fun getAllStates(): List<State>
    fun existsState(stateId: String): Boolean
}