package data.repositories

import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.Project
import domain.repositories.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMapper: ProjectDtoMapper
) : ProjectRepository {

    override suspend fun createProject(project: Project): Boolean {
        when {
            project.name.isEmpty() -> throw UserEnterInvalidValueException("name can't be empty")
            project.description.isEmpty() -> throw UserEnterInvalidValueException("description can't be empty")
        }
        return projectDataSource.addProject(projectMapper.fromEntity(project))
    }

    override suspend fun updateProject(project: Project): Boolean {
        return projectDataSource.updateProject(projectMapper.fromEntity(project))
    }

    override suspend fun deleteProject(projectId: UUID): Boolean {
        return projectDataSource.deleteProject(projectId.toString())
    }

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
            .map { projectRow ->
                projectMapper.toEntity(projectRow)
            }
    }
}