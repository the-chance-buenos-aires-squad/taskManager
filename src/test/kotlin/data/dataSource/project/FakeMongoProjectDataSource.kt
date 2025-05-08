package data.dataSource.project

import data.dto.ProjectDto
import java.util.*

class FakeMongoProjectDataSource:ProjectDataSource {
    private val projects = mutableListOf<ProjectDto>()
    override suspend fun addProject(projectDto: ProjectDto): Boolean {
        return projects.add(projectDto)
    }

    override suspend fun deleteProject(projectId: UUID): Boolean {
        return projects.removeIf { UUID.fromString(it.id) == projectId }
    }

    override suspend fun getProjectById(projectId: UUID): ProjectDto? {
        return projects.find { UUID.fromString(it.id) == projectId }
    }

    override suspend fun updateProject(projectDto: ProjectDto): Boolean {
        val index = projects.indexOfFirst { it.id == projectDto.id }
        return if (index != -1) {
            projects[index] = projectDto
            true
        } else {
            false
        }
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return projects.toList()
    }
}