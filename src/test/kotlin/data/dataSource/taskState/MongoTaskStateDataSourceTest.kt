package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
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
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.bson.conversions.Bson
import java.util.*
import kotlin.test.Test

class MongoTaskStateDataSourceTest {
    private val taskStateCollection: MongoCollection<TaskStateDto> = mockk(relaxed = true)
    private val mongoTaskStateDataSource = MongoTaskStateDataSource(taskStateCollection)
    private val testTaskStateDto = todoDto

    @Test
    fun `should return true when state is created successfully`() = runTest {
        //given
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns true
        coEvery { taskStateCollection.insertOne(any<TaskStateDto>(), any()) } returns insertResult

        //when
        val result = mongoTaskStateDataSource.createTaskState(testTaskStateDto)
        //then
        assertThat(result).isTrue()

    }

    @Test
    fun `should return false when state is not created successfully`() = runTest {
        //given
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns false
        coEvery { taskStateCollection.insertOne(any<TaskStateDto>(), any()) } returns insertResult

        //when
        val result = mongoTaskStateDataSource.createTaskState(testTaskStateDto)
        //then
        assertThat(result).isFalse()

    }

    @Test
    fun `should return true when task state updated successfully`() = runTest {
        // given
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns true
        coEvery {
            taskStateCollection.updateOne(
                any<Bson>(),
                any<Bson>(),
                any()
            )
        } returns updateResult

        //when
        val result = mongoTaskStateDataSource.editTaskState(testTaskStateDto)
        val expectedFilter = Filters.eq(TaskStateDto::id.name, testTaskStateDto.id)
        val expectedUpdates = Updates.combine(
            Updates.set(TaskStateDto::name.name, testTaskStateDto.name)
        )

        //then
        coVerify {
            taskStateCollection.updateOne(
                match<Bson> { it == expectedFilter },
                match<Bson> { it == expectedUpdates },
                any<UpdateOptions>()
            )
        }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when task state didn't updated successfully`() = runTest {
        // given
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns false
        coEvery {
            taskStateCollection.updateOne(
                any<Bson>(),
                any<Bson>(),
                any()
            )
        } returns updateResult

        //when
        val result = mongoTaskStateDataSource.editTaskState(testTaskStateDto)
        val expectedFilter = Filters.eq(TaskStateDto::id.name, testTaskStateDto.id)
        val expectedUpdates = Updates.combine(
            Updates.set(TaskStateDto::name.name, testTaskStateDto.name)
        )

        //then
        coVerify {
            taskStateCollection.updateOne(
                match<Bson> { it == expectedFilter },
                match<Bson> { it == expectedUpdates },
                any<UpdateOptions>()
            )
        }
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() = runTest {
        // given
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns true
        coEvery { taskStateCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        //when
        val result = mongoTaskStateDataSource.deleteTaskState(testTaskStateDto.id)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state is unDeleted`() = runTest {
        // given
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns false
        coEvery { taskStateCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        //when
        val result = mongoTaskStateDataSource.deleteTaskState(testTaskStateDto.id)

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should get all taskStats whe calling getTaskStates`() = runTest {
        val states = listOf(
            testTaskStateDto,
            testTaskStateDto.copy(id = UUID.randomUUID().toString())
        )
        val findFlow = mockk<FindFlow<TaskStateDto>>(relaxed = true)
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<TaskStateDto>>()
            states.forEach { collector.emit(it) }
        }

        //when
        val result = mongoTaskStateDataSource.getTaskStates()

        //then
        coVerify { taskStateCollection.find() }
    }
}