package data.repositories

import data.dataSource.project.ProjectDataSource
import data.repositories.mappers.Mapper
import domain.entities.Project
import domain.repositories.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMapper: Mapper<Project>
) : ProjectRepository {
    override fun createProject(project: Project): Boolean {
        return projectDataSource.addProject(projectMapper.mapEntityToRow(project))
    }

    override fun updateProject(project: Project): Boolean {
        return projectDataSource.updateProject(projectMapper.mapEntityToRow(project))
    }

    override fun deleteProject(projectId: UUID): Boolean {
        return projectDataSource.deleteProject(projectId)
    }

    override fun getProjectById(projectId: UUID): Project? {
        val projectRow = projectDataSource.getProjectById(projectId)
        return projectRow?.let { projectMapper.mapRowToEntity(it) }
    }

    override fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
            .map { projectRow ->
                projectMapper.mapRowToEntity(projectRow)
            }
    }
}