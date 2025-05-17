package domain.usecases.user

import domain.entities.User
import domain.repositories.UserRepository

class CreateUserUseCase(
    private val userRepository: UserRepository,
) {

    suspend fun execute(
        username: String,
        password: String,
    ): User {
        return userRepository.addUser(username, password)
    }
}