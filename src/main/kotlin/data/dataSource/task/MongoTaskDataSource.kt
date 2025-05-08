package data.dataSource.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskDto
import di.MongoCollections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*


class MongoTaskDataSource(
    private val mongoDb: MongoDatabase,
) : TaskDataSource {

    private val taskCollection = mongoDb.getCollection<TaskDto>(MongoCollections.TASKS_COLLECTION)

    override suspend fun addTask(taskDto: TaskDto): Boolean {
        return taskCollection.insertOne(taskDto).wasAcknowledged()
    }

    override suspend fun getTasks(): List<TaskDto> {
        return taskCollection
            .find()
            .toList()

    }

    override suspend fun getTaskById(taskId: UUID): TaskDto? {
        return taskCollection
            .find(
                Filters.eq(TaskDto::id.name,taskId)
            ).firstOrNull()
    }

    override suspend fun deleteTask(taskId: UUID): Boolean {
        return taskCollection
            .deleteOne(
                Filters.eq(TaskDto::id.name,taskId)
            ).wasAcknowledged()
    }

    override suspend fun updateTask(taskDto: TaskDto): Boolean {
        return taskCollection
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
                    Updates.set(TaskDto::updatedAt.name,taskDto.updatedAt),
                )
            ).wasAcknowledged()
    }
}