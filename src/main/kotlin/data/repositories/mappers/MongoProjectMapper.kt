package data.repositories.mappers

import data.dto.ProjectDto
import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class MongoProjectMapper:Mapper<Project,ProjectDto> {
    override fun toMap(entity: Project): ProjectDto {
        return ProjectDto(
            entity.id.toString(),
            entity.name,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun fromMap(row: ProjectDto): Project {
        return Project(
            UUID.fromString(row.id),
            row.name,
            row.description,
            LocalDateTime.parse(row.createdAt)
        )
    }
}