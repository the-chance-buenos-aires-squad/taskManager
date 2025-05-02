package data.repositories

import data.dataSource.TaskStateCSVDataSource
import domain.entities.State
import domain.repositories.TaskStateRepository

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateCSVDataSource
): TaskStateRepository{
    override fun createTaskState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun editTaskState(state: State): Boolean {
        return taskStateCSVDataSource.editTaskState(state)
    }

    override fun deleteTaskState(stateId: String): Boolean {
        return taskStateCSVDataSource.deleteTaskState(stateId)
    }

    override fun getAllTaskStates(): List<State> {
        return taskStateCSVDataSource.getAllTaskStates()
    }

    override fun existsTaskState(stateId: String): Boolean {
        return taskStateCSVDataSource.existsTaskState(stateId)
    }
}