package data.repositories

import data.dataSource.taskState.TaskStateDataSource
import data.repositories.mappers.TaskStateMapper
import domain.entities.TaskState
import domain.repositories.TaskStateRepository
import java.util.UUID

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateDataSource,
    private val taskStateMapper: TaskStateMapper,
) : TaskStateRepository {
    override fun createTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.createTaskState(taskStateMapper.mapEntityToRow(state))
    }

    override fun editTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.editTaskState(taskStateMapper.mapEntityToRow(state))
    }

    override fun deleteTaskState(stateId: UUID): Boolean {
        return taskStateCSVDataSource.deleteTaskState(stateId)
    }

    override fun getAllTaskStates(): List<TaskState> {
        return taskStateCSVDataSource.getAllTaskStates()
    }

    override fun existsTaskState(name: String, projectId: UUID): Boolean {
        return taskStateCSVDataSource.existsTaskState(name, projectId)
    }
}