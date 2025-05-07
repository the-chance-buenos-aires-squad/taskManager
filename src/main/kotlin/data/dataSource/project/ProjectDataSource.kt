package data.dataSource.project

import data.dto.ProjectDto
import java.util.*


interface ProjectDataSource {
    suspend fun addProject(projectDto: ProjectDto): Boolean
    suspend fun deleteProject(projectId: UUID): Boolean
    suspend fun getProjectById(projectId: UUID): ProjectDto?
    suspend fun updateProject(projectDto: ProjectDto): Boolean
    suspend fun getAllProjects(): List<ProjectDto>
}