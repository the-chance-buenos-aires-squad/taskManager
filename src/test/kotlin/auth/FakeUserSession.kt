package auth

import data.exceptions.UserMateNotAllowedException
import domain.entities.User
import domain.entities.UserRole
import presentation.exceptions.UserNotLoggedInException


class FakeUserSession : UserSession {
    private var user: User? = null

    override fun setCurrentUser(user: User?) {
        this.user = user
    }

    override fun getCurrentUser(): User? = user

    override suspend fun <T> runIfLoggedIn(action: suspend (currentUser: User) -> T): T {
        return user?.let { action(it) } ?: throw UserNotLoggedInException()
    }

    override suspend fun <T> runIfUserIsAdmin(action: suspend (currentUser: User) -> T): T {
        val current = user ?: throw UserNotLoggedInException()
        if (current.role == UserRole.MATE) throw UserMateNotAllowedException()
        return action(current)
    }
}