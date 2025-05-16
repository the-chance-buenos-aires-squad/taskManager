package auth

import data.exceptions.UserMateNotAllowedException
import domain.entities.User
import domain.entities.UserRole
import presentation.exceptions.UserNotLoggedInException

class UserSessionImpl :UserSession{
    private var currentUser: User? = null

    override fun setCurrentUser(user: User?) {
        currentUser = user
    }

    override fun getCurrentUser(): User? = currentUser

    override suspend fun <T> runIfLoggedIn(action: suspend (currentUser: User) -> T): T {
        return action(getCurrentUser() ?: throw UserNotLoggedInException())
    }

    override suspend fun <T> runIfUserIsAdmin(action: suspend (currentUser: User) -> T): T {
        return action(getCurrentUser().let {
            it ?: throw UserNotLoggedInException()
            if (it.role == UserRole.MATE) throw UserMateNotAllowedException()
            it
        })
    }
}