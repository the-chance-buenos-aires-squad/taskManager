package data.dataSource.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.UserDto
import di.MongoCollections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

class MongoUserDataSource(
    private val mongoDb: MongoDatabase
) : UserDataSource {

    private val userCollection = mongoDb.getCollection<UserDto>(MongoCollections.USERS_COLLECTION)

    override suspend fun addUser(userDto: UserDto): Boolean {
        return userCollection.insertOne(userDto).wasAcknowledged()
    }

    override suspend fun getUserById(id: UUID): UserDto? {
        return userCollection
            .find(
                Filters.eq(UserDto::_id.name,id)
            ).firstOrNull()
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return userCollection
            .find(
                Filters.eq(UserDto::username.name,userName)
            ).firstOrNull()
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        return userCollection
            .deleteOne(
                Filters.eq(UserDto::_id.name,id)
            ).wasAcknowledged()
    }

    override suspend fun getUsers(): List<UserDto> {
        return userCollection.find().toList()
    }

    override suspend fun updateUser(userDto: UserDto): Boolean {
        return userCollection
            .updateOne(
                Filters.eq(UserDto::_id.name, userDto._id),
                Updates.combine(
                    Updates.set(UserDto::username.name, userDto.username),
                    Updates.set(UserDto::password.name, userDto.password),
                    Updates.set(UserDto::role.name, userDto.role),
                    Updates.set(UserDto::createdAt.name, userDto.createdAt),
                )
            ).wasAcknowledged()
    }

}