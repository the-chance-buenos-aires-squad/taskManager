package data.repositories

import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import domain.entities.Task
import domain.repositories.TaskRepository
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskDtoMapper
) : TaskRepository {

    override suspend fun addTask(task: Task): Boolean {
        return taskDataSource.addTask(taskMapper.fromType(task))
    }

    override suspend fun getAllTasks(): List<Task> {
        val taskRow = taskDataSource.getTasks()
        return taskRow.map { task ->
            taskMapper.toType(task)
        }
    }

    override suspend fun getTaskById(id: UUID): Task? {
        return taskDataSource.getTaskById(id.toString())?.let {
            taskMapper.toType(it)

        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        return taskDataSource.updateTask(taskMapper.fromType(task))

    }

    override suspend fun deleteTask(id: UUID): Boolean {
        return taskDataSource.deleteTask(id.toString())
    }

}