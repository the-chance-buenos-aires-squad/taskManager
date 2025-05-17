package domain.repositories

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

interface TaskRepository {

    suspend fun addTask(
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: String
    ): Boolean

    suspend fun updateTask(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: String,
        createdAt : LocalDateTime,
    ): Boolean

    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(id: UUID): Task?

    suspend fun deleteTask(id: UUID): Boolean
}