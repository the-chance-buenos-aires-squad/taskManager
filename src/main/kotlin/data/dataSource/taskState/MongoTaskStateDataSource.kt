package data.dataSource.taskState

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskStateDto
import di.MongoCollections
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.UUID

class MongoTaskStateDataSource(
    mongoDb: MongoDatabase
) : TaskStateDataSource {

    private val taskStateCollection = mongoDb.getCollection<TaskStateDto>(MongoCollections.TASK_STATES_COLLECTION)

    override suspend fun createTaskState(state: TaskStateDto): Boolean {
        return taskStateCollection.insertOne(state).wasAcknowledged()
    }

    override suspend fun editTaskState(editStateDto: TaskStateDto): Boolean {
        return taskStateCollection.updateOne(
            Filters.eq(TaskStateDto::id.name, editStateDto.id),
            Updates.combine(
                Updates.set(TaskStateDto::name.name, editStateDto.name)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteTaskState(stateId: String): Boolean {
        return taskStateCollection.deleteOne(
            Filters.eq(TaskStateDto::id.name, stateId)
        ).wasAcknowledged()
    }

    override suspend fun getTaskStates(): List<TaskStateDto> {
       return taskStateCollection.find().toList()
    }

    override suspend fun existsTaskState(name: String, projectId: String): Boolean {
        val filter = Filters.and(
            Filters.eq(TaskStateDto::name.name, name),
            Filters.eq(TaskStateDto::projectId.name, projectId)
        )
        val existingTaskState = taskStateCollection.find(filter).firstOrNull()
        return existingTaskState != null
    }


}