package data.dataSource.user

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.UserDto
import di.MongoCollections
import java.util.*

class MongoUserDataSource(
    private val mongoDb: MongoDatabase
) : UserDataSource {

    val userCollection = mongoDb.getCollection<UserDto>(MongoCollections.USERS_COLLECTION)

    override suspend fun addUser(user: UserDto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: UUID): UserDto? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: UserDto): Boolean {
        TODO("Not yet implemented")
    }

}