package data.repositories.mappers

import data.dto.UserDto
import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

class UserDtoMapper : Mapper<User, UserDto> {
    override fun fromType(type: User): UserDto {
        return UserDto(
            id = type.id.toString(),
            username = type.username,
            role = type.role,
            createdAt = type.createdAt.toString()
        )
    }

    override fun toType(row: UserDto): User {
        return User(
            id = UUID.fromString(row.id),
            username = row.username,
            role = row.role ?: UserRole.MATE,
            createdAt = LocalDateTime.parse(row.createdAt)
        )
    }

}