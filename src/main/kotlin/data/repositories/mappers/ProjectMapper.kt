package data.repositories.mappers

import domain.entities.Project
import java.time.LocalDateTime

class ProjectMapper : Mapper<Project> {
    override fun mapEntityToRow(entity: Project): List<String> {
        return listOf(
            entity.id,
            entity.name,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Project {
        return Project(
            id = row[0],
            name = row[1],
            description = row[2],
            createdAt = LocalDateTime.parse(row[3])
        )
    }

}
