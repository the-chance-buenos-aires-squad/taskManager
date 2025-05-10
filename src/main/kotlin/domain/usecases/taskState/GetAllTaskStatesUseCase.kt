package domain.usecases.taskState

import domain.entities.TaskState
import domain.repositories.TaskStateRepository
import java.util.*

class GetAllTaskStatesUseCase(private val repository: TaskStateRepository) {
    suspend fun execute(selectedProject: UUID): List<TaskState> {
        val allStates = repository.getAllTaskStates()
        return allStates.filter { it.projectId == selectedProject }
    }
}