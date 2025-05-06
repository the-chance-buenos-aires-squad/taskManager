package domain.usecases

import domain.entities.Task
import domain.repositories.TaskRepository

class GetTasksUseCase(private val taskRepository: TaskRepository) {
    fun getTasks(): List<Task> {
        return taskRepository.getAllTasks()
    }
}