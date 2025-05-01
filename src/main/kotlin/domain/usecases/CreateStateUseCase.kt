package domain.usecases

import domain.entities.State
import domain.repositories.StateRepository

class CreateStateUseCase(private val repository: StateRepository) {
    fun execute(state: State) = repository.createState(state)
}