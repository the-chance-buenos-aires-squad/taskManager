package data.repositories

import data.dataSource.task.TaskDataSource
import data.repositories.mappers.CsvTaskMapper
import domain.entities.Task
import domain.repositories.TaskRepository
import java.util.*

class TaskRepositoryImpl(
    private val csvTaskDataSource: TaskDataSource,
    private val taskMapper: CsvTaskMapper
) : TaskRepository {

    override fun addTask(task: Task): Boolean {
        val mappedTask = taskMapper.toMap(task)
        return csvTaskDataSource.addTask(mappedTask)
    }

    override fun getAllTasks(): List<Task> {
        val allTasks: List<List<String>> = csvTaskDataSource.getTasks()
        return allTasks.map { row ->
            taskMapper.fromMap(row)
        }
    }

    override fun deleteTask(id: UUID): Boolean {
        return csvTaskDataSource.deleteTask(id.toString())
    }

    override fun updateTask(task: Task): Boolean {
        return csvTaskDataSource.updateTask(taskMapper.toMap(task))
    }

    override fun getTaskById(id: UUID): Task? {
        return csvTaskDataSource.getTaskById(id.toString())?.let {
            taskMapper.fromMap(it)
        }
    }

}