package data.dto


interface DtoParser<type, dto> {
    fun toDto(type: type): dto
    fun fromDto(dto: dto): type
}