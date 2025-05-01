package domain.usecases

import domain.entities.State
import domain.repositories.StateRepository

class EditStateUseCase(private val repository: StateRepository) {
    fun execute(state: State): Boolean {
        return repository.editState(state)
    }
}