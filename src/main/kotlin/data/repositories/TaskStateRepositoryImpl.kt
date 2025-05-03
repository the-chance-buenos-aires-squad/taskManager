package data.repositories

import data.dataSource.taskState.TaskStateCSVDataSource
import data.repositories.mappers.Mapper
import data.repositories.mappers.TaskStateMapper
import domain.entities.Project
import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateCSVDataSource,
//    private val taskStateMapper: TaskStateMapper,
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