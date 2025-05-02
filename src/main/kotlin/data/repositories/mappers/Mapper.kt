package data.repositories.mappers

interface Mapper<entity> {
    fun mapEntityToRow(entity: entity):List<String>

    fun mapRowToEntity(row: List<String>):entity
}