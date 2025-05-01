package data.dataSource

import org.buinos.domain.entities.Project
import java.io.File

class CsvProjectDataSource(private val file: File) : ProjectDataSource {
    override fun saveData(project: Project): Boolean {
        return false
    }

    override fun deleteData(projectId: String): Boolean {
        return false
    }

    override fun findProjectById(projectId: String): Project? {
        return null
    }

    override fun updateProject(project: Project): Boolean {
        return false
    }

    override fun getAllProjects(): List<Project> {
        return emptyList()
    }
}