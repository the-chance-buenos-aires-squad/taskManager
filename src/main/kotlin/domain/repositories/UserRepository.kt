package domain.repositories

import data.dto.UserDto
import domain.entities.User
import java.util.*


interface UserRepository {
    suspend fun addUser(userName: String, password: String): User
    suspend fun getUserByUserName(userName: String): UserDto?
}