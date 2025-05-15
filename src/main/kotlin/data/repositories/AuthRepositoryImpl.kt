package data.repositories

import domain.entities.User
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import presentation.exceptions.UserNotLoggedInException
import kotlin.jvm.Throws

class AuthRepositoryImpl(
    private val userRepository: UserRepository,
) : AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(username: String, password: String): User {
        val user = userRepository.loginUser(username,password)
        currentUser = user
        return user;
    }

    override suspend fun addUser(userName: String, password: String): User {
        return  userRepository.addUser(userName,password)
    }


    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getCurrentUser(): User? = currentUser
    override suspend fun <T> runIfLoggedIn(action: suspend (currentUser:User) -> T): T {
        return action(getCurrentUser()?: throw UserNotLoggedInException())
    }

}