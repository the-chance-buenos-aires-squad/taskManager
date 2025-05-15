package data.dataSource.project

import data.dto.DtoParser
import data.dto.ProjectDto

class ProjectDtoParser : DtoParser<List<String>, ProjectDto> {
    override fun toDto(type: List<String>): ProjectDto {
        return ProjectDto(
            _id = type[ID_ROW],
            name = type[NAME_ROW],
            description = type[DESCRIPTION_ROW],
            createdAt = type[CREATED_AT_ROW]
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

    private companion object {
        private const val ID_ROW = 0
        private const val NAME_ROW = 1
        private const val DESCRIPTION_ROW = 2
        private const val CREATED_AT_ROW = 3
    }
}