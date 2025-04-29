package domain.usecases

import domain.repositories.StateRepository
import org.buinos.domain.entities.State

class DeleteStateUseCase(private val repository: StateRepository) {
    fun execute(stateId: String) = repository.deleteState(stateId)
}