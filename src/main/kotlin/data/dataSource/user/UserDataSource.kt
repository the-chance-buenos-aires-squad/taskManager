package data.dataSource.user

import data.dto.UserDto

interface UserDataSource {

    suspend fun addUser(userDto: UserDto): Boolean
    suspend fun getUserById(id: String): UserDto?
    suspend fun getUserByUserName(userName: String): UserDto?
    suspend fun deleteUser(id: String): Boolean
    suspend fun getUsers(): List<UserDto>
    suspend fun updateUser(userDto: UserDto): Boolean
}