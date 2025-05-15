package domain.usecases.task

import presentation.exceptions.UserNotLoggedInException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.audit.AddAuditUseCase
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val addAuditUseCase: AddAuditUseCase,
    private val authRepository: AuthRepository
) {

    suspend fun execute(id: UUID): Boolean {
        return taskRepository.deleteTask(id)
    }

}