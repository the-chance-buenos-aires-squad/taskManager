package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import domain.entities.User
import domain.entities.UserRole
import java.io.File
import java.time.LocalDateTime

class UserCsvDataSource {

    companion object {
        const val usersFilePath = "src/main/kotlin/data/resource/users_file.csv"
        private val file = File(usersFilePath)
        const val HEADER_ROW = 1
    }

    init {
        createFileWithHeader()
    }


    fun insertUser(user: User): Boolean {
        return try {
            csvWriter().open(file, append = true) {
                writeUserRow(user)
            }
            true
        } catch (e: Exception) {
            println("Failed to write user: ${e.message}")
            false
        }
    }

    fun getUserById(id: String): User? {
        return getUsers().find { it.id == id }
    }

    fun deleteUser(id: String): Boolean {
        val allUsers = this.getUsers()
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

    fun getUsers(): List<User> {
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

     fun updateUser(user: User): Boolean {
        val allUsers = this.getUsers()
        val exists = allUsers.any { it.id == user.id }

        if (!exists) return false

        val updatedUsers = allUsers
            .filterNot { it.id == user.id }
            .toMutableList()
            .apply { add(user) }

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