package domain.usecases

import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.User
import domain.repositories.AuthRepository
import domain.validation.UserValidator

class CreateUserUseCase(
    private val authRepository: AuthRepository,
    private val addAuditUseCase: AddAuditUseCase
) {

    suspend fun addUser(
        username: String,
        password: String,
    ): User {

        val userAdded = authRepository.addUser(username, password)
        addAuditUseCase.addAudit(
            entityId = userAdded.id.toString(),
            entityType = EntityType.USER,
            action = ActionType.CREATE,
            field = "create new user",
            oldValue = null,
            newValue = "creating user:${userAdded.username}",
            userId = "${authRepository.getCurrentUser()!!.id}",
        )
        return userAdded
    }


}