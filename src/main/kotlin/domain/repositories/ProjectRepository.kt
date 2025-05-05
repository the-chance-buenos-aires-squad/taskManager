package domain.repositories

import domain.entities.Project
import java.util.*

interface ProjectRepository {
    fun createProject(project: Project): Boolean
    fun updateProject(project: Project): Boolean
    fun deleteProject(projectId: UUID): Boolean
    fun getProjectById(projectId: UUID): Project?
    fun getAllProjects(): List<Project>
}