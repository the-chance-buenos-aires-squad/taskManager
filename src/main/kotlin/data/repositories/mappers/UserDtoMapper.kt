package data.repositories.mappers

import data.dto.UserDto
import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

class UserDtoMapper : Mapper<User, UserDto> {
    override fun fromEntity(entity: User): UserDto {
        return UserDto(
            id = entity.id.toString(),
            username = entity.username,
            role = entity.role,
            createdAt = entity.createdAt.toString()
        )
    }

    override fun toEntity(type: UserDto): User {
        return User(
            id = UUID.fromString(type.id),
            username = type.username,
            role = type.role ?: UserRole.MATE,
            createdAt = LocalDateTime.parse(type.createdAt)
        )
    }

}