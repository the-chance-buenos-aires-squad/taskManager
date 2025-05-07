package data.repositories.mappers.userMappers

import data.repositories.mappers.Mapper
import domain.entities.User
import domain.entities.UserRole
import java.time.LocalDateTime
import java.util.*

class UserCsvMapper : Mapper<User, List<String>> {

    override fun fromEntity(entity: User): List<String> {
        return listOf(
            entity.id.toString(),
            entity.username,
            entity.password,
            entity.role.name,
            entity.createdAt.toString()
        )
    }

    override fun toEntity(type: List<String>): User {
        return User(
            id = UUID.fromString(type[ID]),
            username = type[USER_NAME],
            password = type[PASSWORD],
            role = UserRole.entries.find { it.name == type[USER_ROLE] }!!,
            createdAt = LocalDateTime.parse(type[USER_CREATED_AT])
        )
    }


    companion object {
        private const val ID = 0
        private const val USER_NAME = 1
        private const val PASSWORD = 2
        private const val USER_ROLE = 3
        private const val USER_CREATED_AT = 4
    }

}