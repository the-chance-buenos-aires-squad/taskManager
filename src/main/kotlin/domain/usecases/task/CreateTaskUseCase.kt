package domain.usecases.task


import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.Task
import domain.repositories.TaskRepository
import java.util.*

class CreateTaskUseCase(
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


        validateTaskTitle(title)
        validateTaskDescription(description)
        validateProjectId(projectId)


        val newTask = Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo
        )


        return taskRepository.addTask(newTask)
    }

    private fun validateTaskTitle(title: String) {
        if (title.trim().isEmpty()) {
            //todo move to cli
            throw TaskTitleEmptyException()
        }
    }

    private fun validateTaskDescription(description: String) {
        if (description.trim().isEmpty()) {
            //todo move to cli
            throw TaskDescriptionEmptyException()
        }
    }

    private fun validateProjectId(projectId: UUID) {
        if (projectId == UUID(0, 0)) {
            //todo move to cli
            throw InvalidProjectIdException()
        }
    }
}