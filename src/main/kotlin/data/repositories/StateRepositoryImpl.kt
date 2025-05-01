package data.repositories

import data.dataSource.StateCSVDataSource
import domain.entities.State
import domain.repositories.StateRepository

class StateRepositoryImpl(
    private val stateCSVDataSource: StateCSVDataSource
): StateRepository{
    override fun createState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun editState(state: State): Boolean {
        return stateCSVDataSource.editState(state)
    }

    override fun deleteState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun existsState(stateId: String): Boolean {
        return stateCSVDataSource.existsState(stateId)
    }
}