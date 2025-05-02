package data.repositories.mappers

import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime

class UserMapper : Mapper<User> {
    override fun mapEntityToRow(entity: User): List<String> {
        return listOf(
            entity.id,
            entity.username,
            entity.password,
            entity.role.name,
            entity.createdAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): User {
        return User(
            id = row[0],
            username = row[1],
            password = row[2],
            role = UserRole.entries.find { it.name == row[3] }!!,
            createdAt = LocalDateTime.parse(row[4])
        )
    }
}