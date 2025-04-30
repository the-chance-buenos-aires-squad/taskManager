package data.repositories

import data.dataSource.StateCSVDataSource
import domain.repositories.StateRepository
import org.buinos.domain.entities.State

class StateRepositoryImpl(
    private val dataSource: StateCSVDataSource
): StateRepository{
    override fun createState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun editState(state: State): Boolean {
        if (state.name.isBlank() || state.id.isBlank() || state.projectId.isBlank()) {
            throw IllegalArgumentException("State failed cannot be empty")
        }

        val allStates = dataSource.getAllStates()
        val existingState = allStates.find { it.id == state.id }

        if (existingState == null) return false

        if (existingState.projectId != state.projectId) return false
        return dataSource.updateState(state)
    }

    override fun deleteState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }
}