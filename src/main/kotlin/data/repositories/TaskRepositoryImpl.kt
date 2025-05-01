package data.repositories

import domain.entities.Task
import domain.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl:TaskRepository {

    override fun createTask(task: Task): Task {
       return task.copy(
            id = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}