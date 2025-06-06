package data.dataSource.user

import data.dataSource.util.CsvHandler
import data.dto.UserDto
import data.repositories.dataSource.UserDataSource
import java.io.File

class CsvUserDataSource(
    private val csvHandler: CsvHandler,
    private val userDtoParser: UserDtoParser,
    private val file: File
) : UserDataSource {


    override suspend fun addUser(userDto: UserDto): Boolean {
        return try {
            csvHandler.write(
                row = userDtoParser.toType(userDto),
                file = file,
                append = true
            )
            true
        } catch (e: Exception) {
            println("Failed to write user : ${e.message}")
            false
        }
    }

    override suspend fun getUserById(id: String): UserDto? {
        return getUsers().find { it.id == id }
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return getUsers().find { it.username == userName }
    }

    override suspend fun deleteUser(id: String): Boolean {
        val allUsers = this.getUsers()
        val updatedUsers = allUsers.filterNot { it.id == id }

        if (allUsers.size == updatedUsers.size) {
            return false
        }

        return try {
            file.writeText("")

            // Write each remaining user back to the file
            updatedUsers.forEach { user ->
                csvHandler.write(
                    row = userDtoParser.toType(user),
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

    override suspend fun getUsers(): List<UserDto> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file).map { row -> userDtoParser.fromType(row) }
    }


    override suspend fun updateUser(userDto: UserDto): Boolean {
        val allUsers = this.getUsers()
        val exists = allUsers.any { it.id == userDto.id }

        if (!exists) return false

        val updatedUsers = allUsers
            .filterNot { it.id == userDto.id }
            .toMutableList()
            .apply { add(userDto) }

        return try {
            file.writeText("")

            updatedUsers.forEach { updatedUser ->
                csvHandler.write(
                    row = userDtoParser.toType(updatedUser),
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
}

