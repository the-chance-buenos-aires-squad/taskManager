package domain.usecases.task

import domain.entities.Task
import domain.repositories.TaskRepository

class GetTasksUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(): List<Task> {
        return taskRepository.getAllTasks()
    }
}