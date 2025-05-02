package domain.usecases

import domain.repositories.TaskStateRepository

class GetAllTaskStatesUseCase (private val repository: TaskStateRepository) {
    fun execute() = repository.getAllStates()
}