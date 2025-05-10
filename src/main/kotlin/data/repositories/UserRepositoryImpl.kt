package data.repositories

import data.dataSource.user.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.User
import domain.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserDtoMapper
) : UserRepository {


    override suspend fun addUser(user: User): Boolean {
        return userDataSource.addUser(userMapper.fromEntity(user))
    }

    override suspend fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(userMapper.fromEntity(user))
    }

    override suspend fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id.toString())
    }

    override suspend fun getUserById(id: UUID): User? {
        return userDataSource.getUserById(id.toString())?.let {
            userMapper.toEntity(it)
        }
    }

    override suspend fun getUserByUserName(userName: String): User? {
        return userDataSource.getUserByUserName(userName)?.let { userMapper.toEntity(it) }
    }

    override suspend fun getUsers(): List<User> {
            val usersRows = userDataSource.getUsers()
            return usersRows.map { userRow ->
                userMapper.toEntity(userRow)
            }
    }


}