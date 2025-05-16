package auth

import data.exceptions.UserMateNotAllowedException
import domain.entities.User
import domain.entities.UserRole
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

    suspend fun <T> runIfUserIsAdmin(action: suspend (currentUser: User) -> T): T {
        return action(getCurrentUser().let {
            it ?: throw UserNotLoggedInException()
            if (it.role == UserRole.MATE) throw UserMateNotAllowedException()
            it
        })
    }
}