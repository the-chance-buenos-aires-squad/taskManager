package domain.usecases.task

import domain.customeExceptions.UserNotLoggedInException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import java.util.*

class UpdateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository
) {
    fun updateTask(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: UUID? = null,
    ): Boolean {

        val currentUser = authRepository.getCurrentUser()
            ?: throw UserNotLoggedInException()

        val updatedTask = Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = currentUser.id,
        )


        return taskRepository.updateTask(updatedTask).also { result ->
            if (result) {
                addAuditUseCase.addAudit(
                    entityId = updatedTask.id.toString(),
                    entityType = EntityType.TASK,
                    action = ActionType.UPDATE,
                    field = "",
                    oldValue = "${updatedTask.id}",
                    newValue = "$updatedTask",
                    userId = currentUser.id.toString()
                )
            }
        }
    }
}