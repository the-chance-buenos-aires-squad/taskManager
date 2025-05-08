package domain.usecases.taskState

import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class CreateTaskStateUseCase(private val repository: TaskStateRepository) {
    suspend fun execute(state: TaskState): Boolean {
        return repository.createTaskState(state)
    }
}