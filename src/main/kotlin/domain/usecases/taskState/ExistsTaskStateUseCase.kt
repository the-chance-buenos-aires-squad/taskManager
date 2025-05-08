package domain.usecases.taskState

import domain.repositories.TaskStateRepository
import java.util.*

class ExistsTaskStateUseCase(private val repository: TaskStateRepository) {
    suspend fun execute(name: String, projectId: UUID): Boolean {
        return repository.existsTaskState(name, projectId)
    }
}