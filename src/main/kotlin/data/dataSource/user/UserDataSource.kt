package data.dataSource.user

import data.dto.UserDto
import java.util.*

interface UserDataSource {

    suspend fun addUser(userDto: UserDto): Boolean
    suspend fun getUserById(id: UUID): UserDto?
    suspend fun getUserByUserName(userName: String): UserDto?
    suspend fun deleteUser(id: UUID): Boolean
    suspend fun getUsers(): List<UserDto>
    suspend fun updateUser(userDto: UserDto): Boolean
}