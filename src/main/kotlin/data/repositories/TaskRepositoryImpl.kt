package data.repositories

import data.dataSource.task.TaskDataSource
import data.repositories.mappers.Mapper
import domain.entities.Task
import domain.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(private val csvTaskDataSource: TaskDataSource,private val taskMapper: Mapper<Task>) :
    TaskRepository {

    override fun createTask(task: Task): Task {
        val newTask = task.copy(id = UUID.randomUUID(),createdAt = LocalDateTime.now(),updatedAt = LocalDateTime.now())
        val mappedTask = taskMapper.mapEntityToRow(newTask)
        csvTaskDataSource.addTask(mappedTask)
        return task
    }
}