package domain.usecases.taskState

import domain.repositories.TaskStateRepository
import java.util.*

class DeleteTaskStateUseCase(private val repository: TaskStateRepository) {
    suspend fun execute(stateId: UUID): Boolean {
        return repository.deleteTaskState(stateId)
    }
}