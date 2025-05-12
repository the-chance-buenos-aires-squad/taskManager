package data.repositories.mappers

import data.dto.ProjectDto
import domain.entities.Project
import java.time.LocalDateTime
import java.util.*

class ProjectDtoMapper : Mapper<Project, ProjectDto> {
    override fun fromEntity(entity: Project): ProjectDto {
        return ProjectDto(
            entity.id.toString(),
            entity.title,
            entity.description,
            entity.createdAt.toString()
        )
    }

    override fun toEntity(type: ProjectDto): Project {
        return Project(
            UUID.fromString(type._id),
            type.title,
            type.description,
            LocalDateTime.parse(type.createdAt)
        )
    }
}