package domain.repositories

import domain.entities.Project

interface ProjectRepository {
    fun createProject(project: Project): Boolean
    fun updateProject(project: Project): Boolean
    fun deleteProject(projectId: String): Boolean
    fun findProjectById(projectId: String): Project?
    fun getAllProjects(): List<Project>
}