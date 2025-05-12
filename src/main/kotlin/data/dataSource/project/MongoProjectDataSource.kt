package data.dataSource.project

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.ProjectDto
import data.repositories.dataSource.ProjectDataSource
import kotlinx.coroutines.flow.toList

class MongoProjectDataSource(
    private val projectCollection: MongoCollection<ProjectDto>
) : ProjectDataSource {

    override suspend fun addProject(projectDto: ProjectDto): Boolean {
        return projectCollection.insertOne(projectDto).wasAcknowledged()
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        return projectCollection.deleteOne(Filters.eq(ProjectDto::_id.name, projectId)).wasAcknowledged()
    }


    override suspend fun updateProject(projectDto: ProjectDto): Boolean {
        return projectCollection.updateOne(
            filter = Filters.eq(ProjectDto::_id.name, projectDto._id),
            update = Updates.combine(
                Updates.set(ProjectDto::title.name, projectDto.title),
                Updates.set(ProjectDto::description.name, projectDto.description),
                Updates.set(ProjectDto::createdAt.name, projectDto.createdAt)
            )
        ).wasAcknowledged()
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return projectCollection.find().toList()
    }

}