package domain.usecases.task

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

    fun deleteTask(id: UUID): Boolean{
        TODO()
    }

}