package domain.usecases

import domain.repositories.TaskStateRepository

class DeleteTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(stateId: String): Boolean {
        return repository.deleteTaskState(stateId)
    }
}