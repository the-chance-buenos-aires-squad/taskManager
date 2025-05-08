package data.repositories.dataSource

import java.util.*


interface ProjectDataSource {
    fun addProject(project: List<String>): Boolean
    fun deleteProject(projectId: UUID): Boolean
    fun getProjectById(projectId: UUID): List<String>?
    fun updateProject(project: List<String>): Boolean
    fun getAllProjects(): List<List<String>>
}