package data.dataSource.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.UserDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class MongoUserDataSource(
    private val userCollection: MongoCollection<UserDto>
) : UserDataSource {

    override suspend fun addUser(userDto: UserDto): Boolean {
        return userCollection.insertOne(userDto).wasAcknowledged()
    }

    override suspend fun getUserById(id: String): UserDto? {
        return userCollection
            .find(
                Filters.eq(UserDto::id.name, id)
            ).firstOrNull()
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return userCollection
            .find(
                Filters.eq(UserDto::username.name, userName)
            ).firstOrNull()
    }

    override suspend fun deleteUser(id: String): Boolean {
        return userCollection
            .deleteOne(Filters.eq(UserDto::id.name, id))
            .wasAcknowledged()
    }

    override suspend fun getUsers(): List<UserDto> {
        return userCollection.find().toList()
    }

    override suspend fun updateUser(userDto: UserDto): Boolean {
        return userCollection
            .updateOne(
                Filters.eq(UserDto::id.name, userDto.id),
                Updates.combine(
                    Updates.set(UserDto::username.name, userDto.username),
                    Updates.set(UserDto::password.name, userDto.password),
                    Updates.set(UserDto::role.name, userDto.role),
                    Updates.set(UserDto::createdAt.name, userDto.createdAt),
                )
            ).wasAcknowledged()
    }

}