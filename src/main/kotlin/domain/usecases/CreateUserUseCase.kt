package domain.usecases

import domain.customeExceptions.*
import domain.entities.User
import domain.repositories.AuthRepository
import domain.util.UserValidator
import java.util.*

class CreateUserUseCase(
    private val authRepository: AuthRepository,
    private val userValidator: UserValidator
) {

    fun addUser(
        username: String,
        password: String,
        confirmPassword: String,
    ) : User{
        userValidator.validateUsername(username)
        userValidator.validatePassword(password, confirmPassword)

        val userAdded = authRepository.addUser( username,password)

        if (userAdded == null) throw CreateUserException()

        return userAdded
    }

}