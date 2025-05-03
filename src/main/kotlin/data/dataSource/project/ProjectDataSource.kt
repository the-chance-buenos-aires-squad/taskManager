package data.dataSource.project

import domain.entities.Project
import java.util.UUID


interface ProjectDataSource {
    fun addProject(project: List<String>): Boolean
    fun deleteProject(projectId: UUID): Boolean
    fun getProjectById(projectId: UUID): Project?
    fun updateProject(project: List<String>): Boolean
    fun getAllProjects(): List<Project>
}