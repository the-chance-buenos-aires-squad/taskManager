package data.repositories.mappers

interface Mapper<T, R> {

    fun fromEntity(entity: T): R

    fun toEntity(row: R): T
}