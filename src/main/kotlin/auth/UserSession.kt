package auth

import domain.entities.User
import presentation.exceptions.UserNotLoggedInException

class UserSession {
    private var currentUser: User? = null

    fun setCurrentUser(user: User?) {
        currentUser = user
    }

    fun getCurrentUser(): User? = currentUser
    suspend fun <T> runIfLoggedIn(action: suspend (currentUser: User) -> T): T {
        return action(getCurrentUser() ?: throw UserNotLoggedInException())
    }
}