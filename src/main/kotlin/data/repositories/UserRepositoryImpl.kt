package data.repositories

import data.dataSource.user.UserDataSource
import data.repositories.mappers.UserMapper
import domain.entities.User
import domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserMapper
) : UserRepository {

    override fun addUser(user: User): Boolean {
        return userDataSource.insertUser(user)
    }

    override fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(userMapper.mapEntityToRow(user))
    }

    override fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id)
    }

    override fun getUserById(id: String): User? {
        return when (val userRow = userDataSource.getUserById(id)) {
            null -> null
            else -> userMapper.mapRowToEntity(userRow)
        }
    }

    override fun getUserByUserName(userName: String): User? {
        return when (val userRow = userDataSource.getUserByUserName(userName)) {
            null -> null
            else -> userMapper.mapRowToEntity(userRow)
        }
    }

    override fun getUsers(): List<User> {
        val usersRows = userDataSource.getUsers()
        return usersRows.map { userRow ->
            userMapper.mapRowToEntity(userRow)
        }
    }

}