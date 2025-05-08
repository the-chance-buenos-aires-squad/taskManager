package data.dataSource.task

import data.dto.TaskDto
import java.util.*

interface TaskDataSource {

    suspend fun addTask(taskDto: TaskDto): Boolean

    suspend fun getTasks(): List<TaskDto>

    suspend fun getTaskById(taskId: UUID): TaskDto?

    suspend fun deleteTask(taskId: UUID): Boolean

    suspend fun updateTask(taskDto: TaskDto): Boolean
}