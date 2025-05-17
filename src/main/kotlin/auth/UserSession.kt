package auth

import domain.entities.User

interface UserSession {
    fun setCurrentUser(user: User?)
    fun getCurrentUser(): User?

    suspend fun <T> runIfLoggedIn(action: suspend (currentUser: User) -> T): T
    suspend fun <T> runIfUserIsAdmin(action: suspend (currentUser: User) -> T): T
}