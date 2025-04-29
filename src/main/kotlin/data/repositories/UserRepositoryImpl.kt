package data.repositories

import data.dataSource.DataSource
import domain.repositories.UserRepository
import org.buinos.domain.entities.User

class UserRepositoryImpl(private val userDataSource: DataSource<User>) : UserRepository {

    override fun insertUser(user: User): Boolean {
        return userDataSource.insertItem(user)
    }

    override fun updateUser(user: User): Boolean {
        return userDataSource.updateItem(user)
    }

    override fun deleteUser(user: User): Boolean {
        return userDataSource.deleteItem(user.id)
    }

    override fun getUserById(id: String): User? {
        return userDataSource.getItemById(id)
    }

    override fun getUsers(): List<User> {
        return userDataSource.getItems()
    }

}