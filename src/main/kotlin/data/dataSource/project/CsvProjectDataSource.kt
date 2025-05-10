package data.dataSource.project


import data.dataSource.util.CsvHandler
import data.dto.ProjectDto
import data.repositories.dataSource.ProjectDataSource
import java.io.File

class CsvProjectDataSource(
    private val file: File,
    private val projectDtoParser: ProjectDtoParser,
    private val csvHandler: CsvHandler,
) : ProjectDataSource {

    override suspend fun addProject(projectDto: ProjectDto): Boolean {
        csvHandler.write(projectDtoParser.fromDto(projectDto), file)
        return true
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        val all = getAllProjects()
        val updated = all.filterNot { it._id == projectId }
        if (all.size == updated.size) return false

        rewriteAllProjects(updated)
        return true
    }

    override suspend fun getProjectById(projectId: String): ProjectDto? {
        return getAllProjects().find { it._id == projectId }
    }

    override suspend fun updateProject(projectDto: ProjectDto): Boolean {
        val all = getAllProjects()
        if (all.none { it._id == projectDto._id }) return false
        val updatedProject = all.map {
            if (it._id == projectDto._id) projectDto else it
        }

        rewriteAllProjects(updatedProject)
        return true
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return csvHandler.read(file).map { projectDtoParser.toDto(it) }
    }

    private fun rewriteAllProjects(projects: List<ProjectDto>) {
        file.writeText("") // clear file
        projects.forEach {
            csvHandler.write(projectDtoParser.fromDto(it), file)
        }
    }
}
