package data.dataSource.project


import data.dataSource.util.CsvHandler
import data.repositories.mappers.ProjectMapper
import domain.entities.Project
import java.io.File

class CsvProjectDataSource(
    private val file: File,
    private val csvHandler: CsvHandler,
    private val projectMapper: ProjectMapper
) : ProjectDataSource {

    override fun saveData(project: Project): Boolean {
        if (findProjectById(project.id) != null) return false
        csvHandler.write(projectMapper.mapEntityToRow(project), file)
        return true
    }

    override fun deleteData(projectId: String): Boolean {
        val all = getAllProjects()
        val updated = all.filterNot { it.id == projectId }
        if (all.size == updated.size) return false

        rewriteAllProjects(updated)
        return true
    }

    override fun findProjectById(projectId: String): Project? {
        return getAllProjects().find { it.id == projectId }
    }

    override fun updateProject(project: Project): Boolean {
        val all = getAllProjects()
        if (all.none { it.id == project.id }) return false
        val updatedProject = all.map {
            if (it.id == project.id) project else it
        }

        rewriteAllProjects(updatedProject)
        return true
    }

    override fun getAllProjects(): List<Project> {
        val rows = csvHandler.read(file)
        return rows.mapNotNull {
            try {
                projectMapper.mapRowToEntity(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun rewriteAllProjects(projects: List<Project>) {
        file.writeText("") // clear file
        projects.forEach {
            csvHandler.write(projectMapper.mapEntityToRow(it), file)
        }
    }
}
