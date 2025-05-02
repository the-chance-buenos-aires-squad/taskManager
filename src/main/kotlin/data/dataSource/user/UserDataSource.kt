package data.dataSource.user

import java.util.UUID

interface UserDataSource {

    fun addUser(userRow: List<String>): Boolean
    fun getUserById(id: UUID): List<String>?
    fun getUserByUserName(userName: String): List<String>?
    fun deleteUser(id: UUID): Boolean
    fun getUsers(): List<List<String>>
    fun updateUser(userRow: List<String>): Boolean

}