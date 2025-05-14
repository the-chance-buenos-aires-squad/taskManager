package data.dataSource.project

import data.dto.DtoParser
import data.dto.ProjectDto

class ProjectDtoParser : DtoParser<List<String>, ProjectDto> {
    override fun toDto(type: List<String>): ProjectDto {
        return ProjectDto(
            _id = type[0],
            name = type[1],
            description = type[2],
            createdAt = type[3]
        )
    }

    override fun fromDto(dto: ProjectDto): List<String> {
        return listOf(
            dto._id,
            dto.name,
            dto.description,
            dto.createdAt
        )
    }
}