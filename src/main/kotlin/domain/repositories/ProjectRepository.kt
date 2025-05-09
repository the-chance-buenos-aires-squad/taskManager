package domain.repositories

import domain.entities.Project
import java.util.*

interface ProjectRepository {
    suspend fun createProject(project: Project): Boolean
    suspend fun updateProject(project: Project): Boolean
    suspend fun deleteProject(projectId: UUID): Boolean
    suspend fun getProjectById(projectId: UUID): Project?
    suspend fun getAllProjects(): List<Project>
}