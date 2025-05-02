package domain.usecases

import domain.entities.State
import domain.repositories.TaskStateRepository

class EditTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(editState: State): Boolean {
        return repository.editTaskState(editState)
    }
}