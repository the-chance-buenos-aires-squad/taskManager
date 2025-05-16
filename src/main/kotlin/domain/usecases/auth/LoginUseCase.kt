package domain.usecases.auth

import domain.entities.User
import domain.repositories.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend fun execute(
        username: String,
        password: String
    ): User {
        val user = authRepository.login(username, password)
        return user
    }
}
