package data.repositories.mappers

import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class CsvProjectMapper : Mapper<Project, List<String>> {

    override fun toMap(entity: Project): List<String> {
        return listOf(
            entity.id.toString(),
            entity.name,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun fromMap(row: List<String>): Project {
        return Project(
            id = UUID.fromString(row[ID]),
            name = row[NAME],
            description = row[DESCRIPTION],
            createdAt = LocalDateTime.parse(row[CREATED_AT])
        )
    }

    companion object ProjectColumns {
        private const val ID = 0
        private const val NAME = 1
        private const val DESCRIPTION = 2
        private const val CREATED_AT = 3
    }

}
