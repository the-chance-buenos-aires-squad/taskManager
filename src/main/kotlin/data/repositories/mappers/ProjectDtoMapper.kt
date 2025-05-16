package data.repositories.mappers

import data.dto.ProjectDto
import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class ProjectDtoMapper : Mapper<Project, ProjectDto> {
    override fun fromType(type: Project): ProjectDto {
        return ProjectDto(
            type.id.toString(),
            type.title,
            type.description,
            type.createdAt.toString()
        )
    }

    override fun toType(row: ProjectDto): Project {
        return Project(
            UUID.fromString(row._id),
            row.name,
            row.description,
            LocalDateTime.parse(row.createdAt)
        )
    }
}