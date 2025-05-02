package domain.usecases

import domain.customeExceptions.InvalidCredentialsException
import domain.entities.User
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.util.MD5Hasher
import domain.util.UserValidator

class AuthenticationUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    fun login(
        username: String,
        password: String
    ): User {

        val userValidator = UserValidator()
        userValidator.validateUsername(username)

        val user = getUserOrThrow(username)

        checkHashPassword(user, password)
        authRepository.login(user)
        return user
    }

    private fun getUserOrThrow(username: String): User {
        return userRepository.getUserByUserName(username.trim())
            ?: throw InvalidCredentialsException()
    }

    private fun checkHashPassword(user: User, inputPassword: String) {
        val md5Hash = MD5Hasher()
        val hashedInput = md5Hash.hash(inputPassword)
        if (user.password != hashedInput) {
            throw InvalidCredentialsException()
        }
    }

}