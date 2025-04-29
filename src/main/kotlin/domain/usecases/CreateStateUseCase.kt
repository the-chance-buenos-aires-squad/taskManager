package domain.usecases

import domain.repositories.StateRepository
import org.buinos.domain.entities.State

class CreateStateUseCase(private val repository: StateRepository) {
    fun execute(state: State) = repository.createState(state)
}