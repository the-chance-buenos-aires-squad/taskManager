package domain.usecases.taskState

import domain.repositories.TaskStateRepository

class ExistsTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(stateId: String): Boolean {
        return repository.existsTaskState(stateId)
    }
}