package domain.usecases.task

import domain.customeExceptions.UserNotLoggedInException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import java.time.LocalDateTime
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
        assignedTo: UUID?=null,
        createdBy: UUID
    ): Boolean {
        val updatedTask = Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = createdBy,
        )

        val currentUser = authRepository.getCurrentUser()
            ?: throw UserNotLoggedInException()

        return taskRepository.updateTask(updatedTask).also { result->
            if (result){
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