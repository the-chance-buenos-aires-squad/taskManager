package data.repositories.dataSource

import data.dto.TaskDto
import java.util.*

interface TaskDataSource {

    suspend fun addTask(taskDto: TaskDto): Boolean

    suspend fun getTasks(): List<TaskDto>

    suspend fun getTaskById(taskId: String): TaskDto?

    suspend fun deleteTask(taskId: String): Boolean

    suspend fun updateTask(taskDto: TaskDto): Boolean
}