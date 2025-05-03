package data.dataSource.project


import data.dataSource.util.CsvHandler
import domain.entities.Project
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class CsvProjectDataSource(
    private val file: File,
    private val csvHandler: CsvHandler,
) : ProjectDataSource {

    override fun addProject(project: List<String>): Boolean {
        csvHandler.write(project, file)
        return true
    }

    override fun deleteProject(projectId: UUID): Boolean {
        val all = getAllProjects()
        val updated = all.filterNot { it.id == projectId }
        if (all.size == updated.size) return false

        rewriteAllProjects(updated)
        return true
    }

    override fun getProjectById(projectId: UUID): Project? {
        return getAllProjects().find { it.id == projectId }
    }

    override fun updateProject(project: List<String>): Boolean {
        val all = getAllProjects()
        if (all.none { it.id.toString() == project[0] }) return false
        val updatedProject = all.map {
            Project(UUID.fromString(project[0]), project[1], project[2], LocalDateTime.parse(project[3]))
        }

        rewriteAllProjects(updatedProject)
        return true
    }

    override fun getAllProjects(): List<Project> {
        val rows = csvHandler.read(file)
        return rows.mapNotNull {
            try {
                Project(UUID.fromString(it[0]), it[1], it[2], LocalDateTime.parse(it[3]))
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun rewriteAllProjects(projects: List<Project>) {
        file.writeText("") // clear file
        projects.forEach {
            csvHandler.write(listOf(it.id.toString(), it.name, it.description, it.createdAt.toString()), file)
        }
    }
}
