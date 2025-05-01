package domain.usecases

import domain.repositories.StateRepository

class GetAllStatesUseCase (private val repository: StateRepository) {
    fun execute() = repository.getAllStates()
}