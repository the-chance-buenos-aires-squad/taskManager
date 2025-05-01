package domain.repositories

import domain.entities.User


interface UserRepository {

    fun insertUser(user: User): Boolean
    fun updateUser(user: User): Boolean
    fun deleteUser(user: User): Boolean
    fun getUserById(id: String): User?
    fun getUserByUserName(userName: String): User?
    fun getUsers(): List<User>


}