package domain.usecases

import domain.customeExceptions.InvalidCredentialsException
import domain.customeExceptions.UserNameEmptyException
import domain.entities.User
import domain.repositories.UserRepository
import domain.util.MD5Hash

class AuthenticationUseCase(private val repository: UserRepository) {

    fun login(username: String, password: String): User {
        validateUsername(username)
        val user = getUserOrThrow(username)
        checkHashPassword(user, password)
        return user
    }

    private fun validateUsername(username: String) {
        if (username.isBlank()) {
            throw UserNameEmptyException()
        }
    }

    private fun getUserOrThrow(username: String): User {
        return repository.getUserByUserName(username.trim())
            ?: throw InvalidCredentialsException()
    }

    private fun checkHashPassword(user: User, inputPassword: String) {
        val hashedInput = MD5Hash.hash(inputPassword)
        if (user.password != hashedInput) {
            throw InvalidCredentialsException()
        }
    }

}