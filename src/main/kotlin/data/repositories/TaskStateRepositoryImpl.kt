package data.repositories

import data.exceptions.TaskStateNameException
import data.repositories.dataSource.TaskStateDataSource
import data.repositories.mappers.TaskStateDtoMapper
import domain.entities.TaskState
import domain.repositories.TaskStateRepository
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateCSVDataSource: TaskStateDataSource,
    private val taskStateDtoMapper: TaskStateDtoMapper,
) : TaskStateRepository {
    override suspend fun createTaskState(state: TaskState): Boolean {
        val taskStates =
            getAllTaskStates().filter { it.projectId == state.projectId && it.title.lowercase() == state.title.lowercase() }
        if (!taskStates.isEmpty()) throw TaskStateNameException()
        return taskStateCSVDataSource.createTaskState(taskStateDtoMapper.fromType(state))
    }

    override suspend fun editTaskState(state: TaskState): Boolean {
        return taskStateCSVDataSource.editTaskState(taskStateDtoMapper.fromType(state))
    }

    override suspend fun deleteTaskState(stateId: UUID): Boolean {
        return taskStateCSVDataSource.deleteTaskState(stateId.toString())
    }

    override suspend fun getAllTaskStates(): List<TaskState> {
        val taskStateRows = taskStateCSVDataSource.getTaskStates()
        return taskStateRows.map { taskStateRow -> taskStateDtoMapper.toType(taskStateRow) }
    }

}