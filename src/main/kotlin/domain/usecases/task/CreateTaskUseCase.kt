package domain.usecases.task


import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import java.util.*

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository
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



        return taskRepository.addTask(newTask).apply {
            val currentUser = authRepository.getCurrentUser()
            if (this && currentUser!=null)addAuditUseCase.addAudit(
                entityId = newTask.id.toString(),
                entityType = EntityType.TASK,
                action = ActionType.CREATE,
                field = "",
                oldValue = "",
                newValue = "creating task:${newTask.title}",
                userId = currentUser.id.toString()
            )

        }
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