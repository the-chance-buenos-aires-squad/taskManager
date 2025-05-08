package data.dataSource


interface DtoParser<type,dto>{
    fun toDto(type: type):dto
    fun fromDto(dto: dto):type
}