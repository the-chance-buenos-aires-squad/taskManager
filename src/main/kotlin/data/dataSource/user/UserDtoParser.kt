package data.dataSource.user

import data.dataSource.DtoParser
import data.dto.UserDto
import domain.entities.UserRole


class UserDtoParser : DtoParser<List<String>, UserDto> {

    override fun toDto(type: List<String>): UserDto {
        return UserDto(
            id = type[ID_ROW],
            username = type[USER_NAME_ROW],
            password = type[PASSWORD_ROW],
            role = UserRole.entries.find { it.name == type[USER_ROLE_ROW]},
            createdAt = type[USER_CREATED_AT_ROW]
        )
    }

    override fun fromDto(dto: UserDto): List<String> {
        return listOf(dto.id,dto.username,dto.password,dto.role!!.name,dto.createdAt)
    }

    companion object {
        private const val ID_ROW = 0
        private const val USER_NAME_ROW = 1
        private const val PASSWORD_ROW = 2
        private const val USER_ROLE_ROW = 3
        private const val USER_CREATED_AT_ROW = 4
    }

}