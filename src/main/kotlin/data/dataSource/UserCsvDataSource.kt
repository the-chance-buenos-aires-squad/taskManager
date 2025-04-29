package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.buinos.domain.entities.User
import org.buinos.domain.entities.UserRole
import java.io.File
import java.time.LocalDateTime

class UserCsvDataSource : DataSource<User> {

    companion object {
        const val usersFilePath = "src/main/kotlin/data/resource/users_file.csv"
        private val file = File(usersFilePath)
        const val HEADER_ROW = 1
    }

    init {
        createFileWithHeader()
    }


    override fun insertItem(item: User): Boolean {
        return try {
            csvWriter().open(file, append = true) {
                writeUserRow(item)
            }
            true
        } catch (e: Exception) {
            println("Failed to write user: ${e.message}")
            false
        }
    }

    override fun getItemById(id: String): User? {
        return getItems().find { it.id == id }
    }

    override fun deleteItem(id: String): Boolean {
        val allUsers = this.getItems()
        val updatedUsers = allUsers.filterNot { it.id == id }

        if (allUsers.size == updatedUsers.size) {
            return false
        }

        return try {
            csvWriter().open(file) {
                writeHeaderRow()
                updatedUsers.forEach { user ->
                    writeUserRow(user)
                }
            }
            true
        } catch (e: Exception) {
            println("Error during delete: ${e.message}")
            false
        }
    }

    override fun getItems(): List<User> {
        if (!file.exists()) return emptyList()
        return csvReader().readAll(file).drop(HEADER_ROW).map { row ->
            User(
                id = row[0],
                username = row[1],
                password = row[2],
                role = UserRole.valueOf(row[3]),
                createdAt = LocalDateTime.parse(row[4])
            )
        }
    }

    override fun updateItem(item: User): Boolean {
        val allUsers = this.getItems()
        val exists = allUsers.any { it.id == item.id }

        if (!exists) return false

        val updatedUsers = allUsers
            .filterNot { it.id == item.id }
            .toMutableList()
            .apply { add(item) }

        return try {
            csvWriter().open(File(usersFilePath)) {
                writeHeaderRow()
                updatedUsers.forEach { user ->
                    writeUserRow(user)
                }
            }
            true
        } catch (e: Exception) {
            println("Error updating user: ${e.message}")
            false
        }
    }

    private fun createFileWithHeader() {
        try {
            if (!file.exists()) {
                file.createNewFile()
                csvWriter().open(file) {
                    writeHeaderRow()
                }
            }
        } catch (e: Exception) {
            println("Failed to initialize CSV file: ${e.message}")
        }
    }

    private fun ICsvFileWriter.writeUserRow(user: User) {
        writeRow(listOf(user.id, user.username, user.password, user.role, user.createdAt))
    }

    private fun ICsvFileWriter.writeHeaderRow() {
        writeRow(listOf("id", "username", "password", "role", "createdAt"))
    }
}