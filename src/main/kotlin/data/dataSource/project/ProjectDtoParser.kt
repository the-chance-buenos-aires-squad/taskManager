package data.dataSource.project

import data.dto.ProjectDto
import data.repositories.mappers.Mapper

class ProjectDtoParser : Mapper<List<String>, ProjectDto> {
    override fun fromType(type: List<String>): ProjectDto {
        return ProjectDto(
            _id = type[0],
            name = type[1],
            description = type[2],
            createdAt = type[3]
        )
    }

    override fun toType(row: ProjectDto): List<String> {
        return listOf(
            row._id,
            row.name,
            row.description,
            row.createdAt
        )
    }
}