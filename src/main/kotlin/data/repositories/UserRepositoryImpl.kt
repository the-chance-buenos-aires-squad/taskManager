package data.repositories

import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserMapper
import domain.entities.User
import domain.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserMapper
) : UserRepository {


    override fun addUser(user: User): Boolean {
        return userDataSource.addUser(userMapper.mapEntityToRow(user))
    }

    override fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(userMapper.mapEntityToRow(user))
    }

    override fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id)
    }

    override fun getUserById(id: UUID): User? {
        return userDataSource.getUserById(id)?.let {
            userMapper.mapRowToEntity(it)
        }
    }

    override fun getUserByUserName(userName: String): User? {
        return userDataSource.getUserByUserName(userName)?.let { userMapper.mapRowToEntity(it) }
    }

    override fun getUsers(): List<User> {
        val usersRows = userDataSource.getUsers()
        return usersRows.map { userRow ->
            userMapper.mapRowToEntity(userRow)
        }
    }


}