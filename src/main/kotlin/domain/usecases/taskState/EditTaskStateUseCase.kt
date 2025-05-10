package domain.usecases.taskState

import domain.entities.TaskState
import domain.repositories.TaskStateRepository

class EditTaskStateUseCase(private val repository: TaskStateRepository) {
    suspend fun execute(editState: TaskState): Boolean {
        return repository.editTaskState(editState)
    }
}