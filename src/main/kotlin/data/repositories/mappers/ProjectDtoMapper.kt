package data.repositories.mappers

import data.dto.ProjectDto
import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class ProjectDtoMapper:Mapper<Project,ProjectDto> {
    override fun fromEntity(entity: Project): ProjectDto {
        return ProjectDto(
            entity.id.toString(),
            entity.name,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun toEntity(row: ProjectDto): Project {
        return Project(
            UUID.fromString(row.id),
            row.name,
            row.description,
            LocalDateTime.parse(row.createdAt)
        )
    }
}