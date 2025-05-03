package data.repositories.mappers

import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class ProjectMapper : Mapper<Project> {
    override fun mapEntityToRow(entity: Project): List<String> {
        return listOf(
            entity.id.toString(),
            entity.name,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Project {
        return Project(
            id = UUID.fromString(row[ProjectColumns.ID]),
            name = row[ProjectColumns.NAME],
            description = row[ProjectColumns.DESCRIPTION],
            createdAt = LocalDateTime.parse(row[ProjectColumns.CREATED_AT])
        )
    }

    private object ProjectColumns {
        const val ID = 0
        const val NAME = 1
        const val DESCRIPTION = 2
        const val CREATED_AT = 3
    }

}
