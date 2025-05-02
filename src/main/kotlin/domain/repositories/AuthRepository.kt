package domain.repositories

import domain.entities.User

interface AuthRepository {
    fun login( username: String, password: String) : User?
    fun addUser(userName: String,password: String) : User?
    fun logout()
    fun getCurrentUser(): User?
}