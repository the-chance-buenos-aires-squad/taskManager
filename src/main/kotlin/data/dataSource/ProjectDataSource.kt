package data.dataSource

import org.buinos.domain.entities.Project

interface ProjectDataSource {
    fun saveData(project: Project):Boolean
    fun deleteData(projectId:String):Boolean
    fun findProjectById(projectId: String):Project?
    fun updateProject(project: Project):Boolean
    fun getAllProjects():List<Project>
}