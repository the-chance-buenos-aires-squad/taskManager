package domain.usecases.taskState

import domain.repositories.TaskStateRepository
import java.util.UUID

class ExistsTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(name: String, projectId: UUID): Boolean {
         return repository.existsTaskState(name, projectId)
    }
}