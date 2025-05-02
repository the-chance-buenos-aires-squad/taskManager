package domain.usecases

import domain.entities.State
import domain.repositories.TaskStateRepository

class CreateTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(state: State) : Boolean {
return repository.createTaskState(state)   }
}