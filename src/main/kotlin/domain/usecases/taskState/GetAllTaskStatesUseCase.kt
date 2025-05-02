package domain.usecases.taskState

import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class GetAllTaskStatesUseCase(private val repository: TaskStateRepository) {
    fun execute(): List<TaskState> {
        return repository.getAllTaskStates()
    }
}