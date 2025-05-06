package domain.usecases.taskState

import domain.repositories.TaskStateRepository
import java.util.UUID

class DeleteTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(stateId: UUID): Boolean {
        return repository.deleteTaskState(stateId)
    }
}