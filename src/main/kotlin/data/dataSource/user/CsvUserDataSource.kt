package data.dataSource.user

import data.dataSource.util.CsvHandler
import java.io.File
import java.util.*

class CsvUserDataSource(
    private val csvHandler: CsvHandler,
    private val file: File
) : UserDataSource {


    override fun addUser(userRow: List<String>): Boolean {
        return try {
            csvHandler.write(
                row = userRow,
                file = file,
                append = true
            )
            true
        } catch (e: Exception) {
            println("Failed to write user: ${e.message}")
            false
        }
    }

    override fun getUserById(id: UUID): List<String>? {
        return getUsers().find { it[ID_ROW] == id.toString() }
    }

    override fun getUserByUserName(userName: String): List<String>? {
        return getUsers().find { it[USER_NAME_ROW] == userName }
    }

    override fun deleteUser(id: UUID): Boolean {
        val allUsers = this.getUsers()
        val updatedUsers = allUsers.filterNot { it[ID_ROW] == id.toString() }

        if (allUsers.size == updatedUsers.size) {
            return false // No user with the given ID was found
        }

        return try {
            // Clear the file before rewriting
            file.writeText("") // truncate file contents

            // Write each remaining user back to the file
            updatedUsers.forEach { userRow ->
                csvHandler.write(
                    row = listOf(
                        userRow[ID_ROW] ?: "",
                        userRow[USER_NAME_ROW] ?: "",
                        userRow[PASSWORD_ROW] ?: "",
                        userRow[USER_ROLE_ROW] ?: "",
                        userRow[USER_CREATED_AT_ROW] ?: ""
                    ),
                    file = file,
                    append = true
                )
            }

            true
        } catch (e: Exception) {
            println("Error during delete: ${e.message}")
            false
        }
    }

    override fun getUsers(): List<List<String>> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
    }

    override fun updateUser(userRow: List<String>): Boolean {
        val allUsers = this.getUsers()
        val exists = allUsers.any { it[ID_ROW] == userRow[ID_ROW] }

        if (!exists) return false

        val updatedUsers = allUsers
            .filterNot { it[ID_ROW] == userRow[ID_ROW] }
            .toMutableList()
            .apply { add(userRow) }

        return try {
            // Clear the file before rewriting all users
            file.writeText("") // truncate file

            updatedUsers.forEach { updatedUser ->
                csvHandler.write(
                    row = listOf(
                        updatedUser[ID_ROW] ?: "",
                        updatedUser[USER_NAME_ROW] ?: "",
                        updatedUser[PASSWORD_ROW] ?: "",
                        updatedUser[USER_ROLE_ROW] ?: "",
                        updatedUser[USER_CREATED_AT_ROW] ?: ""
                    ),
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

    companion object {
        private const val ID_ROW = 0
        private const val USER_NAME_ROW = 1
        private const val PASSWORD_ROW = 2
        private const val USER_ROLE_ROW = 3
        private const val USER_CREATED_AT_ROW = 4
    }
}