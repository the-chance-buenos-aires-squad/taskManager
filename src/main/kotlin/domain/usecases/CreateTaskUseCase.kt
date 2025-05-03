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

    fun createTask(
        id : UUID = UUID.randomUUID(),
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: UUID? = null,
        createdBy: UUID
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
            assignedTo = assignedTo,
            createdBy = createdBy,
        )


        //TODO: implement audit log for task creation
        return taskRepository.addTask(newTask)
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

    private fun validateProjectId(projectId: UUID) {
        if (projectId == UUID(0, 0)) {
            throw InvalidProjectIdException()
        }
    }
}