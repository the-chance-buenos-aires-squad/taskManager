package domain.usecases

import domain.repositories.TaskStateRepository

class DeleteTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(stateId: String) = repository.deleteState(stateId)
}