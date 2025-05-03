package domain.repositories

import domain.entities.User
import java.util.*


interface UserRepository {

    fun addUser(user: User): Boolean
    fun updateUser(user: User): Boolean
    fun deleteUser(user: User): Boolean
    fun getUserById(id: UUID): User?
    fun getUserByUserName(userName: String): User?
    fun getUsers(): List<User>


}