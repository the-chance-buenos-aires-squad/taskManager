package data.dataSource.user

import data.dto.UserDto
import java.util.*

class FakeMongoUserDataSource : UserDataSource {

    private val users = mutableListOf<UserDto>()

    override suspend fun addUser(userDto: UserDto): Boolean {
       return users.add(userDto) }

    override suspend fun getUserById(id: UUID): UserDto? {
        return users.find { it._id == id.toString() }
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return users.find { it.username == userName }
    }

    override suspend fun deleteUser(id: UUID): Boolean  {
       return users.removeIf { it._id == id.toString() }
    }

    override suspend fun getUsers(): List<UserDto>  {
       return users.toList()
    }

    override suspend fun updateUser(userDto: UserDto): Boolean  {
        val index = users.indexOfFirst { it._id == userDto._id }
        return if (index != -1) {
            users[index] = userDto
            true
        } else {
            false
        }
    }
}