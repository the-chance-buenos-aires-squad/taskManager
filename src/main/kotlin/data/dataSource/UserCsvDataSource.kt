package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.client.ICsvFileWriter
import data.util.CsvHandler
import domain.entities.User
import domain.entities.UserRole
import java.io.File
import java.time.LocalDateTime

class UserCsvDataSource (
    private val file: File,
    private val csvHandler: CsvHandler
){

    companion object {
        const val usersFilePath = "src/main/kotlin/data/resource/users_file.csv"
//        private val file = File(usersFilePath)
        const val HEADER_ROW = 1
    }

    init {
        createFileWithHeader()
    }


    fun insertUser(user: User): Boolean {
        return try {
//            csvWriter().open(file, append = true) {
//                writeUserRow(user)
//            }
//            listOf(user.id, user.username, user.password, user.role, user.createdAt)
            csvHandler.write(
                list = listOf(user.id, user.username, user.password, user.role.name, user.createdAt.toString()),
                file = file,
                append = true)
            true
        } catch (e: Exception) {
            println("Failed to write user: ${e.message}")
            false
        }
    }

    fun getUserById(id: String): User? {
        return getUsers().find { it.id == id }
    }

    fun getUserByUserName(userName: String): User? {
        return getUsers().find { it.username == userName }
    }

    fun deleteUser(id: String): Boolean {
        val allUsers = this.getUsers()
        val updatedUsers = allUsers.filterNot { it.id == id }

        if (allUsers.size == updatedUsers.size) {
            return false
        }

        return try {
//            csvWriter().open(file) {
////                writeHeaderRow()
//                updatedUsers.forEach { user ->
//                    writeUserRow(user)
//                }
//            }
            csvHandler.writeHeaderIfNotExist(listOf("id", "username", "password", "role", "createdAt"), file)
            updatedUsers.forEach { user ->
                csvHandler.write(
                    list = listOf(user.id, user.username, user.password, user.role.name, user.createdAt.toString()),
                    file = file,
                    append = true)
            }

            true
        } catch (e: Exception) {
            println("Error during delete: ${e.message}")
            false
        }
    }

    fun getUsers(): List<User> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file).drop(HEADER_ROW).map { row ->
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
//            csvWriter().open(File(usersFilePath)) {
//                writeHeaderRow()
//                updatedUsers.forEach { user ->
//                    writeUserRow(user)
//                }
//            }
            csvHandler.writeHeaderIfNotExist(listOf("id", "username", "password", "role", "createdAt"), file)
            updatedUsers.forEach { updatedUser->
                csvHandler.write(
                    list= listOf(updatedUser.id, updatedUser.username, updatedUser.password, updatedUser.role.name, updatedUser.createdAt.toString()),
                    file = file,
                    append = true
                )

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
//                csvWriter().open(file) {
//                    writeHeaderRow()
//                }
                csvHandler.writeHeaderIfNotExist(listOf("id", "username", "password", "role", "createdAt"), file)
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