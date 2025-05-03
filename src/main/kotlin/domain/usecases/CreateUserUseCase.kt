package domain.usecases

import domain.customeExceptions.CreateUserException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.User
import domain.repositories.AuthRepository
import domain.util.UserValidator

class CreateUserUseCase(
    private val authRepository: AuthRepository,
    private val userValidator: UserValidator,
    private val addAuditUseCase: AddAuditUseCase
) {

    fun addUser(
        username: String,
        password: String,
        confirmPassword: String,
    ): User {
        userValidator.validateUsername(username)
        userValidator.validatePassword(password, confirmPassword)

        val userAdded = authRepository.addUser(username, password) ?: throw CreateUserException()
        addAuditUseCase.addAudit(
            entityId = userAdded.id.toString(),
            entityType = EntityType.USER,
            action = ActionType.CREATE,
            field = "create new user",
            oldValue = null,
            newValue = "creating user:${userAdded.username}",
            userId = "${authRepository.getCurrentUser()?.id}",
        )
        return userAdded
    }

}