package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.TaskStateDto
import dummyData.dummyStateData.DummyTaskState.todoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.bson.conversions.Bson
import java.util.*
import kotlin.test.Test

class MongoTaskStateDataSourceTest {
    private val taskStateCollection: MongoCollection<TaskStateDto> = mockk(relaxed = true)
    private val testDto = todoDto
    private val mongoTaskStateDataSource = spyk(
        MongoTaskStateDataSource(taskStateCollection),
        recordPrivateCalls = true
    )

    @Test
    fun `createTaskState returns true when insert is acknowledged`() = runTest {
        val insertResult = mockk<InsertOneResult>()
        coEvery { mongoTaskStateDataSource.getTaskStates() } returns emptyList()
        coEvery { insertResult.wasAcknowledged() } returns true
        coEvery { taskStateCollection.insertOne(any<TaskStateDto>(), any()) } returns insertResult

        val result = mongoTaskStateDataSource.createTaskState(testDto)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state is not created successfully`() = runTest {
        val insertResult = mockk<InsertOneResult>()
        coEvery { mongoTaskStateDataSource.getTaskStates() } returns emptyList()
        coEvery { insertResult.wasAcknowledged() } returns false
        coEvery { taskStateCollection.insertOne(any<TaskStateDto>(), any()) } returns insertResult

        val result = mongoTaskStateDataSource.createTaskState(testDto)

        assertThat(result).isFalse()
    }


    @Test
    fun `should return false when task state didn't updated successfull`() = runTest {
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns false
        coEvery {
            taskStateCollection.updateOne(any<Bson>(), any<Bson>(), any<UpdateOptions>())
        } returns updateResult

        val result = mongoTaskStateDataSource.editTaskState(testDto)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() = runTest {
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns true
        coEvery { taskStateCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        val result = mongoTaskStateDataSource.deleteTaskState(testDto._id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state is unDeleted`() = runTest {
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns false
        coEvery { taskStateCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        val result = mongoTaskStateDataSource.deleteTaskState(testDto._id)

        assertThat(result).isFalse()
    }

    @Test
    fun `should get all taskStats whe calling getTaskStates`() = runTest {
        // given
        val states = listOf(
            testDto,
            testDto.copy(_id = UUID.randomUUID().toString())
        )
        val findFlow = mockk<FindFlow<TaskStateDto>>(relaxed = true)
        coEvery { taskStateCollection.find() } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<TaskStateDto>>()
            states.forEach { collector.emit(it) }
        }

        // when
        val result = mongoTaskStateDataSource.getTaskStates()

        // then
        coVerify { taskStateCollection.find() }
        assertThat(result).isEqualTo(states)
    }

}