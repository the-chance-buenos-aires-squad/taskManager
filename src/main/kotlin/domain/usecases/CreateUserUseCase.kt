package domain.usecases

import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository

class CreateUserUseCase(private val repository: UserRepository) {

    fun createUser(username: String, password: String,confirmPassword: String,userRole : UserRole = UserRole.MATE) {
    }


}