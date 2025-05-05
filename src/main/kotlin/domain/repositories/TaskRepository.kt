package domain.repositories

import domain.entities.Task
import java.util.*

interface TaskRepository {
    fun addTask(task: Task): Boolean
    fun getAllTasks(): List<Task>
    fun getTaskById(id: UUID): Task?
    fun updateTask(task: Task): Boolean
    fun deleteTask(id: UUID): Boolean
}