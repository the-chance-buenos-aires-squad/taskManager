package domain.usecases

import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class AuthenticationUseCase(private val repository: UserRepository) {

    fun login(username: String, password: String): User {
        //todo return user
        return User(id = UUID.randomUUID().toString(), username = username, password = password , role = UserRole.ADMIN, createdAt = LocalDateTime.now())
    }


}