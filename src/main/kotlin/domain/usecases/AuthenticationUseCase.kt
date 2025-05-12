package domain.usecases

import domain.entities.User
import domain.repositories.AuthRepository
import domain.util.UserValidator

class AuthenticationUseCase(
    private val authRepository: AuthRepository,
    private val userValidator: UserValidator
) {

    suspend fun login(
        username: String,
        password: String
    ): User {
        //userValidator.validateUsername(username)
        //todo handle uer input form cli
        val user = authRepository.login(username, password)
        return user
    }
}
