package domain.repositories

import domain.entities.User

interface AuthRepository {
    suspend fun login(username: String, password: String): User
    suspend fun addUser(userName: String, password: String): User
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    suspend fun <T>runIfLoggedIn(action: suspend (currentUser:User) -> T):T
}