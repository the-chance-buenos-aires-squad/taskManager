package data.dataSource.project

import data.dto.ProjectDto
import data.repositories.mappers.Mapper

class ProjectDtoParser : Mapper<List<String>, ProjectDto> {
    override fun fromType(type: List<String>): ProjectDto {
        return ProjectDto(
            _id = type[ID_ROW],
            name = type[NAME_ROW],
            description = type[DESCRIPTION_ROW],
            createdAt = type[CREATED_AT_ROW]
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

    private companion object {
        private const val ID_ROW = 0
        private const val NAME_ROW = 1
        private const val DESCRIPTION_ROW = 2
        private const val CREATED_AT_ROW = 3
    }
}