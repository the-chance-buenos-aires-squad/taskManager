package data.dataSource.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskDto
import di.MongoCollections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*


class MongoTaskDataSource(
    private val tasksCollection:MongoCollection<TaskDto>
) : TaskDataSource {


    override suspend fun addTask(taskDto: TaskDto): Boolean {
        return tasksCollection.insertOne(taskDto).wasAcknowledged()
    }

    override suspend fun getTasks(): List<TaskDto> {
        return tasksCollection
            .find()
            .toList()

    }

    override suspend fun getTaskById(taskId: UUID): TaskDto? {
        return tasksCollection
            .find(
                Filters.eq(TaskDto::id.name,taskId)
            ).firstOrNull()
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        return tasksCollection
            .deleteOne(
                Filters.eq(TaskDto::id.name,taskId)
            ).wasAcknowledged()
    }

    override suspend fun updateTask(taskDto: TaskDto): Boolean {
        return tasksCollection
            .updateOne(
                Filters.eq(TaskDto::id.name,taskDto.id),
                Updates.combine(
                    Updates.set(TaskDto::title.name,taskDto.title),
                    Updates.set(TaskDto::description.name,taskDto.description),
                    Updates.set(TaskDto::projectId.name,taskDto.projectId),
                    Updates.set(TaskDto::stateId.name,taskDto.stateId),
                    Updates.set(TaskDto::assignedTo.name,taskDto.assignedTo),
                    Updates.set(TaskDto::createdBy.name,taskDto.createdBy),
                    Updates.set(TaskDto::createdAt.name,taskDto.createdAt),
                    Updates.set(TaskDto::updatedAt.name,taskDto.updatedAt)
                )
            ).wasAcknowledged()
    }
}