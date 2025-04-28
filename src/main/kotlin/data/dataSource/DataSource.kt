package data.dataSource

interface DataSource <T> {

    fun insertItem(item: T ) : Boolean

    fun getItemById(id: String): T?

    fun updateItem(item: T) : Boolean

    fun deleteItem(id: String) : Boolean

    fun getItems(): List<T>
}