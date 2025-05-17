package domain.repositories

import domain.entities.User

interface AuthRepository {
    suspend fun login(username: String, password: String): User
    suspend fun logout()
}