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
        TODO("Not yet implemented")
    }

    override fun deleteState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }
}