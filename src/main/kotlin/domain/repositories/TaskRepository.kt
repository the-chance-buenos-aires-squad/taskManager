package domain.repositories

import domain.entities.Task
import java.util.*

interface TaskRepository {

    suspend fun addTask(task: Task): Boolean
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(id: UUID): Task?
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(id: UUID): Boolean
}