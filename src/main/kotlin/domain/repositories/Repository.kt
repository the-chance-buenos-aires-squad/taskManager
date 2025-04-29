package domain.repositories

import org.buinos.domain.entities.User

interface Repository<T> {

    fun addItem(item: T): Boolean
    fun updateItem(item: T): Boolean
    fun deleteItem(item: T): Boolean
    fun getItemById(id: String): T?
    fun getItems(): List<T>
}