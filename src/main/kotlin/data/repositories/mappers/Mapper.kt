package data.repositories.mappers

interface Mapper<T, R> {

    fun fromType(type: T): R

    fun toType(row: R): T
}