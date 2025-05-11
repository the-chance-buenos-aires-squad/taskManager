package domain.usecases

import domain.customeExceptions.InvalidCredentialsException
import domain.entities.User
import domain.repositories.AuthRepository
import domain.validation.UserValidator

class AuthenticationUseCase(
    private val authRepository: AuthRepository,
    private val userValidator: UserValidator
) {

    suspend fun login(
        username: String,
        password: String
    ): User {
        userValidator.validateUsername(username)
        val user = authRepository.login(username, password) ?: throw InvalidCredentialsException()
        return user
    }
}
