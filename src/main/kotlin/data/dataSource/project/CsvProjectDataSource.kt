package data.dataSource.project


import data.dataSource.util.CsvHandler
import java.io.File
import java.util.*

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
        val updated = all.filterNot { it[0] == projectId.toString() }
        if (all.size == updated.size) return false

        rewriteAllProjects(updated)
        return true
    }

    override fun getProjectById(projectId: UUID): List<String>? {
        return getAllProjects().find { it[0] == projectId.toString() }
    }

    override fun updateProject(project: List<String>): Boolean {
        val all = getAllProjects()
        if (all.none { it[0] == project[0] }) return false
        val updatedProject = all.map {
            if (it[0] == project[0]) project else it
        }

        rewriteAllProjects(updatedProject)
        return true
    }

    override fun getAllProjects(): List<List<String>> {
        if (!file.exists()) return emptyList()
        return csvHandler.read(file)
    }

    private fun rewriteAllProjects(projects: List<List<String>>) {
        file.writeText("") // clear file
        projects.forEach {
            csvHandler.write(it, file)
        }
    }
}
