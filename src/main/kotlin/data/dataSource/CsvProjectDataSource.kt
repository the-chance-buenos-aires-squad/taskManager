package data.dataSource


import data.dataSource.util.CsvHandler
import data.dataSource.util.toCsvRow
import data.dataSource.util.toProject
import domain.entities.Project
import java.io.File

class CsvProjectDataSource(private val file: File, private val csvHandler: CsvHandler) : ProjectDataSource {
    init {
        initializeFileIfNeeded()
    }

    override fun saveData(project: Project): Boolean {
        if (findProjectById(project.id) != null) return false
        csvHandler.write(project.toCsvRow(), file)
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
        return rows.drop(1).mapNotNull {
            try {
                it.toProject()
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun initializeFileIfNeeded() {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    private fun rewriteAllProjects(projects: List<Project>) {
        file.writeText("") // clear file
        projects.forEach {
            csvHandler.write(it.toCsvRow(), file)
        }
    }
}
