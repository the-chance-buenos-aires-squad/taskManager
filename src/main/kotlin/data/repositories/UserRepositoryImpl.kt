package data.repositories

import data.dataSource.UserCsvDataSource
import domain.repositories.Repository
import org.buinos.domain.entities.User

class UserRepositoryImpl(private val userDataSource: UserCsvDataSource) : Repository<User> {
    override fun addItem(item: User): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: User): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: User): Boolean {
        TODO("Not yet implemented")
    }

    override fun getItemById(id: String): User? {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<User> {
        TODO("Not yet implemented")
    }


}