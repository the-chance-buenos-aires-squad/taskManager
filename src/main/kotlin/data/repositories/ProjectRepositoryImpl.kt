package data.repositories

import data.dataSource.project.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.entities.Project
import domain.repositories.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMapper: ProjectDtoMapper
) : ProjectRepository {

    override suspend fun createProject(project: Project): Boolean {
        return projectDataSource.addProject(projectMapper.fromEntity(project))
    }

    override suspend fun updateProject(project: Project): Boolean {
        return projectDataSource.updateProject(projectMapper.fromEntity(project))
    }

    override suspend fun deleteProject(projectId: UUID): Boolean {
        return projectDataSource.deleteProject(projectId.toString())
    }

    override suspend fun getProjectById(projectId: UUID): Project? {
        val projectRow = projectDataSource.getProjectById(projectId.toString())
        return projectRow?.let { projectMapper.toEntity(it) }
    }

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
            .map { projectRow ->
                projectMapper.toEntity(projectRow)
            }
    }
}