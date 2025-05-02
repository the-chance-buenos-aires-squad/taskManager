package domain.usecases

import domain.entities.State
import domain.repositories.TaskStateRepository

class GetAllTaskStatesUseCase (private val repository: TaskStateRepository) {
    fun execute() : List<State> {
        TODO("")
    }
}