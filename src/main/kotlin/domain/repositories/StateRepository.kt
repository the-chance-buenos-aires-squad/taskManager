package domain.repositories

import org.buinos.domain.entities.State

interface StateRepository {
    fun createState(state: State): Boolean
    fun editState(state: State): Boolean
    fun deleteState(stateId: String): Boolean
    fun getAllStates(): List<State>
//    fun getStatesByProject(projectId: String): List<State>
}