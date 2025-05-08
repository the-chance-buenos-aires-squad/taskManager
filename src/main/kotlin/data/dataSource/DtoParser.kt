package data.dataSource

import data.dto.UserDto

interface DtoParser<type,dto>{
    fun toDto(type: type):dto
    fun fromDto(dto: dto):type
}