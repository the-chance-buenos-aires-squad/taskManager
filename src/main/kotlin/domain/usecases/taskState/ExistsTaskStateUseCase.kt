package domain.usecases.taskState

import domain.repositories.TaskStateRepository
import java.util.*

class ExistsTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(name: String, projectId: String): Boolean {
        return repository.existsTaskState(name, projectId)
    }
}