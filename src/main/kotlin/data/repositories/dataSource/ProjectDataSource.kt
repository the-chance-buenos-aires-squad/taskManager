package data.repositories.dataSource

import data.dto.ProjectDto


interface ProjectDataSource {
    suspend fun addProject(projectDto: ProjectDto): Boolean
    suspend fun deleteProject(projectId: String): Boolean
    suspend fun updateProject(projectDto: ProjectDto): Boolean
    suspend fun getAllProjects(): List<ProjectDto>
}