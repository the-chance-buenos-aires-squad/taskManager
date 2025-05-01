package domain.usecases

import domain.repositories.StateRepository

class DeleteStateUseCase(private val repository: StateRepository) {
    fun execute(stateId: String) = repository.deleteState(stateId)
}