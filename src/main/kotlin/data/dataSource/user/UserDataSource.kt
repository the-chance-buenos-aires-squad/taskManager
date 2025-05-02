package data.dataSource.user

import domain.entities.User

interface UserDataSource {

    fun addUser(userRow: List<String>): Boolean
    fun getUserById(id: String): List<String>?
    fun getUserByUserName(userName: String): List<String>?
    fun deleteUser(id: String): Boolean
    fun getUsers(): List<List<String>>
    fun updateUser(userRow: List<String>): Boolean

}