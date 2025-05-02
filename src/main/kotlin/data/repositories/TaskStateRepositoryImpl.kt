package data.repositories

import data.dataSource.TaskStateCSVDataSource
import domain.entities.State
import domain.repositories.TaskStateRepository

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateCSVDataSource
): TaskStateRepository{
    override fun createState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun editState(state: State): Boolean {
        return taskStateCSVDataSource.editTaskState(state)
    }

    override fun deleteState(stateId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun existsState(stateId: String): Boolean {
        return taskStateCSVDataSource.existsTaskState(stateId)
    }
}