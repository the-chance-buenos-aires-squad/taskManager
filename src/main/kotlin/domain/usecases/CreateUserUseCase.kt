package domain.usecases

import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.UserAlreadyExistException
import domain.customeExceptions.UserNameEmptyException
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository
import domain.util.MD5Hash
import java.time.LocalDateTime
import java.util.*

class CreateUserUseCase(private val repository: UserRepository) {
    fun createUser(
        username: String,
        password: String,
        confirmPassword: String,
        userRole: UserRole = UserRole.MATE
    ) {
        validateUsername(username)
        validatePassword(password, confirmPassword)

        if (repository.getUserByUserName(username.trim()) != null) throw UserAlreadyExistException()

        val hashedPassword = MD5Hash.hash(password)

        val newUser = User(
            id = UUID.randomUUID().toString(),
            username = username.trim(),
            password = hashedPassword,
            role = userRole,
            createdAt = LocalDateTime.now()
        )

        repository.insertUser(newUser)
    }

    private fun validateUsername(username: String) {
        if (username.isBlank()) {
            throw UserNameEmptyException()
        }
    }

    private fun validatePassword(password: String, confirmPassword: String) {
        if (password.length < 6) {
            throw InvalidLengthPasswordException()
        }
        if (password != confirmPassword) {
            throw InvalidConfirmPasswordException()
        }
    }
}