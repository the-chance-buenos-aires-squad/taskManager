package domain.usecases

import domain.repositories.StateRepository
import org.buinos.domain.entities.State

class EditStateUseCase(private val repository: StateRepository) {
    fun execute(state: State): Boolean {
        TODO("Not yet implemented")
    }
}