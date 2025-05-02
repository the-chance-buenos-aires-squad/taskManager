package data.repositories

import data.dataSource.TaskStateCSVDataSource
import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateCSVDataSource
): TaskStateRepository{
    override fun createTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.createTaskState(state)
    }

    override fun editTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.editTaskState(state)
    }

    override fun deleteTaskState(stateId: String): Boolean {
        return taskStateCSVDataSource.deleteTaskState(stateId)
    }

    override fun getAllTaskStates(): List<TaskState> {
        return taskStateCSVDataSource.getAllTaskStates()
    }

    override fun existsTaskState(stateId: String): Boolean {
        return taskStateCSVDataSource.existsTaskState(stateId)
    }
}