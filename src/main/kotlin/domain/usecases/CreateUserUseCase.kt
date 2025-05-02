package domain.usecases

import domain.customeExceptions.*
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.util.MD5Hasher
import domain.util.UserValidator
import java.time.LocalDateTime
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    fun addUser(
        username: String, password: String, confirmPassword: String, userRole: UserRole = UserRole.MATE
    ) {

        val userValidator = UserValidator()
        userValidator.validateUsername(username)
        userValidator.validatePassword(password, confirmPassword)

        if (userRepository.getUserByUserName(username.trim()) != null) throw UserAlreadyExistException()

        val mD5Hash = MD5Hasher()
        val hashedPassword = mD5Hash.hash(password)

        val newUser = User(
            id = UUID.randomUUID().toString(),
            username = username.trim(),
            password = hashedPassword,
            role = userRole,
            createdAt = LocalDateTime.now()
        )

        if (authRepository.getCurrentUser() == null) throw CreateUserException()

            userRepository.addUser(newUser)

    }
}