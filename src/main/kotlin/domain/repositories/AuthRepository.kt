package domain.repositories

import domain.entities.User

interface AuthRepository {
    fun login(user: User)
    fun logout()
    fun getCurrentUser(): User?
}