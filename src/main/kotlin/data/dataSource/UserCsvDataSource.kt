package data.dataSource

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.buinos.domain.entities.User
import java.io.File

class UserCsvDataSource : DataSource<User> {

    init {
        createFileWithHeaders()
    }

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


    private fun createFileWithHeaders() {
        val file = File(usersFilePath)

        if (!file.exists()) {
            val isCreated = file.createNewFile()

            if (isCreated) {
                val userHeader: List<String> = listOf("id", "username", "password", "role", "createdAt")
                csvWriter().open(file) {
                    writeRow(userHeader)
                }
                println("File created and header written: $userHeader")
            } else {
                println("Failed to create file")
            }
        } else {
            println("File already exists.")
        }
    }

    companion object {
        const val usersFilePath = "src/main/kotlin/data/resource/users_file.csv"
    }

}