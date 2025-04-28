package data.dataSource

import org.buinos.domain.entities.User

class UserCsvDataSource : DataSource<User>{

    override fun insertItem(item: User): Boolean {
        TODO("Not yet implemented")
    }

    override fun getItemById(id: String): User? {
        TODO("Not yet implemented")
    }

    override fun deleteItem(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<User> {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: User): Boolean {
        TODO("Not yet implemented")
    }

}