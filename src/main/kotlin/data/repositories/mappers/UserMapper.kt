package data.repositories.mappers

import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

class UserMapper : Mapper<User> {
    override fun mapEntityToRow(entity: User): List<String> {
        return listOf(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.role.name,
            entity.createdAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): User {
        return User(
            id = UUID.fromString(row[0]),
            username = row[1],
            password = row[2],
            role = UserRole.entries.find { it.name == row[3] }!!,
            createdAt = LocalDateTime.parse(row[4])
        )
    }
}