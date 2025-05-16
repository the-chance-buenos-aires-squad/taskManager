package domain.usecases.task

import domain.repositories.TaskRepository
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {

    suspend fun execute(id: UUID): Boolean {
        return taskRepository.deleteTask(id)
    }

}