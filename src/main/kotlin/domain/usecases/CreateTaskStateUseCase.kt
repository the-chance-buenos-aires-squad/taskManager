package domain.usecases

import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class CreateTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(state: TaskState): Boolean {
        return repository.createTaskState(state)
    }
}