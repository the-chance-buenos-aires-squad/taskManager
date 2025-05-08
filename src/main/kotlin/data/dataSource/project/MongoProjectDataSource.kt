package data.dataSource.project

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.ProjectDto
import di.MongoCollections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

class MongoProjectDataSource(
    mongoDb: MongoDatabase,
) : ProjectDataSource {

    private val projectCollection = mongoDb.getCollection<ProjectDto>(MongoCollections.PROJECTS_COLLECTION)

    override suspend fun addProject(projectDto: ProjectDto): Boolean {
        return projectCollection.insertOne(projectDto).wasAcknowledged()
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        return projectCollection.deleteOne(Filters.eq(ProjectDto::_id.name, projectId)).wasAcknowledged()
    }

    override suspend fun getProjectById(projectId: String): ProjectDto? {
        return projectCollection.find(Filters.eq(ProjectDto::_id.name, projectId)).firstOrNull()
    }

    override suspend fun updateProject(projectDto: ProjectDto): Boolean {
        return projectCollection.updateOne(
            Filters.eq(ProjectDto::_id.name,projectDto._id),
            Updates.combine(
                Updates.set(ProjectDto::name.name,projectDto.name),
                Updates.set(ProjectDto::description.name,projectDto.description),
                Updates.set(ProjectDto::createdAt.name,projectDto.createdAt)
            )
        ).wasAcknowledged()
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return projectCollection.find().toList()
    }

}