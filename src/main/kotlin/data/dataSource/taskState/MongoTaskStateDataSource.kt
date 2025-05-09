package data.dataSource.taskState

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.TaskStateDto
import data.exceptions.TaskStateNameException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import javax.naming.InvalidNameException

class MongoTaskStateDataSource(
    private val taskStateCollection: MongoCollection<TaskStateDto>
) : TaskStateDataSource {


    override suspend fun createTaskState(state: TaskStateDto): Boolean {
        return taskStateCollection.insertOne(state).wasAcknowledged()
    }

    override suspend fun editTaskState(editState: TaskStateDto): Boolean {
        return taskStateCollection.updateOne(
            Filters.eq(TaskStateDto::id.name, editState.id),
            Updates.combine(
                Updates.set(TaskStateDto::name.name, editState.name)
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




}