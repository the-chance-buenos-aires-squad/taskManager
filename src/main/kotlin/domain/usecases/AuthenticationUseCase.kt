package domain.usecases

import domain.entities.User
import domain.repositories.AuthRepository
import domain.validation.UserValidator

class AuthenticationUseCase(
    private val authRepository: AuthRepository,
) {

    suspend fun login(
        username: String,
        password: String
    ): User {
        val user = authRepository.login(username, password)
        return user
    }
}
