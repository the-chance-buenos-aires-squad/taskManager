package data.repositories

import data.dataSource.taskState.TaskStateDataSource
import data.repositories.mappers.TaskStateDtoMapper
import domain.entities.TaskState
import domain.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateDataSource,
    private val taskStateDtoMapper: TaskStateDtoMapper,
) : TaskStateRepository {
    override suspend fun createTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.createTaskState(taskStateDtoMapper.fromEntity(state))
    }

    override suspend fun editTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.editTaskState(taskStateDtoMapper.fromEntity(state))
    }

    override suspend fun deleteTaskState(stateId: UUID): Boolean {
        return taskStateCSVDataSource.deleteTaskState(stateId.toString())
    }

    override suspend fun getAllTaskStates(): List<TaskState> {
        val taskStateRows = taskStateCSVDataSource.getTaskStates()
        return taskStateRows.map { taskStateRow -> taskStateDtoMapper.toEntity(taskStateRow)}
    }

    override suspend fun existsTaskState(name: String, projectId: UUID): Boolean {
        return taskStateCSVDataSource.existsTaskState(name, projectId.toString())
    }
}