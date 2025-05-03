package data.repositories

import data.dataSource.task.TaskDataSource
import data.repositories.mappers.Mapper
import data.repositories.mappers.TaskMapper
import domain.entities.Task
import domain.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(
    private val csvTaskDataSource: TaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override fun addTask(task: Task): Boolean {
        val mappedTask = taskMapper.mapEntityToRow(task)
        return csvTaskDataSource.addTask(mappedTask)
    }

    override fun getAllTasks(): List<Task> {
        val allTasks: List<List<String>> = csvTaskDataSource.getTasks()
        return allTasks.map { row->
            taskMapper.mapRowToEntity(row)
        }
    }

}