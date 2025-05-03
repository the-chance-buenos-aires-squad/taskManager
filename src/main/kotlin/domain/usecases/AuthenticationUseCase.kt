package domain.usecases

import domain.customeExceptions.InvalidCredentialsException
import domain.entities.User
import domain.repositories.AuthRepository
import domain.util.UserValidator

class AuthenticationUseCase(
    private val authRepository: AuthRepository,
    private val userValidator: UserValidator
) {

    fun login(
        username: String,
        password: String
    ): User {
        userValidator.validateUsername(username)
        val user = authRepository.login(username, password)
        if (user == null){
            throw InvalidCredentialsException()
        }
            return user
    }
}
