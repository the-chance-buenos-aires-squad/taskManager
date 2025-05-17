package domain.usecases.task


import domain.entities.Task
import domain.repositories.TaskRepository
import domain.repositories.UserRepository
import java.util.*

class AddTaskUseCase(
    private val taskRepository: TaskRepository,
) {

    suspend fun execute(
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: String,
    ): Boolean {

        return taskRepository.addTask(
            title,
            description,
            projectId,
            stateId,
            assignedTo
        )
    }

}