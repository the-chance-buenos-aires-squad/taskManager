package domain.usecases.task

import domain.customeExceptions.UserNotLoggedInException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository
) {

    fun deleteTask(id: UUID): Boolean {
        val currentUser = authRepository.getCurrentUser()
            ?: throw UserNotLoggedInException()
        return taskRepository.deleteTask(id).also { result ->
            if (result) {
                addAuditUseCase.addAudit(
                    entityId = id.toString(),
                    entityType = EntityType.TASK,
                    action = ActionType.DELETE,
                    field = "deleting task",
                    oldValue = "task with id:$id",
                    newValue = "",
                    userId = currentUser.id.toString()
                )
            }
        }


    }

}