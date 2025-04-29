package data.repositories

import data.dataSource.DataSource
import domain.repositories.Repository
import org.buinos.domain.entities.User

class UserRepositoryImpl(private val userDataSource: DataSource<User>) : Repository<User> {

    override fun addItem(user: User): Boolean {
        return userDataSource.insertItem(user)
    }

    override fun updateItem(user: User): Boolean {
        return userDataSource.updateItem(user)
    }

    override fun deleteItem(user: User): Boolean {
        return userDataSource.deleteItem(user.id)
    }

    override fun getItemById(id: String): User? {
        return userDataSource.getItemById(id)
    }

    override fun getItems(): List<User> {
       return userDataSource.getItems()
    }


}