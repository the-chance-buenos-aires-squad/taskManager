package data.repositories

import data.dataSource.project.ProjectDataSource
import data.repositories.mappers.Mapper
import domain.entities.Project
import domain.repositories.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val csvProjectMapper: Mapper<Project,List<String>>
) : ProjectRepository {
    override fun createProject(project: Project): Boolean {
        return projectDataSource.addProject(csvProjectMapper.toMap(project))
    }

    override fun updateProject(project: Project): Boolean {
        return projectDataSource.updateProject(csvProjectMapper.toMap(project))
    }

    override fun deleteProject(projectId: UUID): Boolean {
        return projectDataSource.deleteProject(projectId)
    }

    override fun getProjectById(projectId: UUID): Project? {
        val projectRow = projectDataSource.getProjectById(projectId)
        return projectRow?.let { csvProjectMapper.fromMap(it) }
    }

    override fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
            .map { projectRow ->
                csvProjectMapper.fromMap(projectRow)
            }
    }
}