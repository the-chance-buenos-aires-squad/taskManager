package data.repositories

import data.dataSource.UserCsvDataSource
import domain.entities.User
import domain.repositories.UserRepository

class UserRepositoryImpl(private val userDataSource: UserCsvDataSource) : UserRepository {

    override fun addUser(user: User): Boolean {
        return userDataSource.insertUser(user)
    }

    override fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(user)
    }

    override fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id)
    }

    override fun getUserById(id: String): User? {
        return userDataSource.getUserById(id)
    }

    override fun getUserByUserName(userName: String): User? {
        return userDataSource.getUserByUserName(userName)
    }

    override fun getUsers(): List<User> {
        return userDataSource.getUsers()
    }

}