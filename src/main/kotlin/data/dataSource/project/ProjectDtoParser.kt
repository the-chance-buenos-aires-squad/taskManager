package data.dataSource.project

import data.dataSource.DtoParser
import data.dto.ProjectDto

class ProjectDtoParser: DtoParser<List<String>,ProjectDto> {
    override fun toDto(type: List<String>): ProjectDto {
        return ProjectDto(
            id=type[0],
            name = type[1],
            description = type[2],
            createdAt = type[4]
        )
    }

    override fun fromDto(dto: ProjectDto): List<String> {
        return listOf(
            dto.id,
            dto.name,
            dto.description,
            dto.createdAt
        )
    }
}