package data.repositories

import data.dataSource.ProjectDataSource
import domain.entities.Project
import domain.repositories.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {
    override fun createProject(project: Project): Boolean {
        return projectDataSource.saveData(project)
    }

    override fun updateProject(project: Project): Boolean {
        return projectDataSource.updateProject(project)
    }

    override fun deleteProject(projectId: String): Boolean {
        return projectDataSource.deleteData(projectId)
    }

    override fun findProjectById(projectId: String): Project? {
        return projectDataSource.findProjectById(projectId)
    }

    override fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }
}