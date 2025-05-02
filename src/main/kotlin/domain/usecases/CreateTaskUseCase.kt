package domain.usecases


import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.Task
import domain.repositories.TaskRepository
import java.util.*

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
) {

    fun createTask(title: String,
        description: String,
        projectId: UUID,
        stateId: UUID? = null,
        assignedTo: UUID? = null,
        createdBy: UUID): Task {

        validateTaskTitle(title)
        validateTaskDescription(description)
        validateProjectId(projectId)

        //TODO: implement get default stateId if not provided
        val stateId = stateId ?: UUID.randomUUID()
        val newTask = Task(
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = createdBy,
        )

        val task = taskRepository.createTask(newTask)

        //TODO: implement audit log for task creation
        return task
    }

    private fun validateTaskTitle(title: String) {
        if (title.trim().isEmpty()) {
            throw TaskTitleEmptyException()
        }
    }

    private fun validateTaskDescription(description: String) {
        if (description.trim().isEmpty()) {
            throw TaskDescriptionEmptyException()
        }
    }

    //TODO: implement real projectId validation
    private fun validateProjectId(projectId: UUID) {
        if (projectId == UUID(0,0)) {
            throw InvalidProjectIdException()
        }
    }
}