package domain.usecases

import domain.repositories.StateRepository

class DeleteStateUseCase(private val repository: StateRepository) {
    fun execute(stateId: String) {
        TODO("Not yet implemented")
    }
}