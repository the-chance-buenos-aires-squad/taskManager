package domain.usecases

import domain.repositories.StateRepository
import org.buinos.domain.entities.State

class GetAllStatesUseCase (private val repository: StateRepository) {
    fun execute() = repository.getAllStates()
}