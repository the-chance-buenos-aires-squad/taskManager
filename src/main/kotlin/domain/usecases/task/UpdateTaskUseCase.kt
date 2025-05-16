package domain.usecases.task

import domain.entities.Task
import domain.repositories.TaskRepository
import java.util.*

class UpdateTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun execute(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: UUID? = null,
    ): Boolean {

        val updatedTask = Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
        )

        return taskRepository.updateTask(updatedTask)
    }
}