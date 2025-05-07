package domain.repositories

import domain.entities.User
import java.util.*


interface UserRepository {

    suspend fun addUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
    suspend fun deleteUser(user: User): Boolean
    suspend fun getUserById(id: UUID): User?
    suspend fun getUserByUserName(userName: String): User?
    suspend fun getUsers(): List<User>
}