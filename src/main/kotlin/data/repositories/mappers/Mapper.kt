package data.repositories.mappers

interface Mapper<T,R> {

    fun toMap(entity: T): R

    fun fromMap(row: R): T
}