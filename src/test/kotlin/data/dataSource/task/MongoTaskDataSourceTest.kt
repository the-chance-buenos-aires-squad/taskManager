package data.dataSource.task

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
import data.dataSource.dummyData.DummyTasks.validTaskDto
import data.dto.TaskDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.bson.conversions.Bson
import org.junit.jupiter.api.Test
import java.util.*

class MongoTaskDataSourceTest {

    private val taskCollection: MongoCollection<TaskDto> = mockk(relaxed = true)
    private var mongoTaskDataSource = MongoTaskDataSource(taskCollection)
    private val sampleTaskDto: TaskDto = validTaskDto


    @Test
    fun `addTask should return true when insertion is acknowledged`() = runTest {
        // given
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns true
        coEvery { taskCollection.insertOne(any<TaskDto>(), any()) } returns insertResult
        // When
        val result = mongoTaskDataSource.addTask(sampleTaskDto)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `addTask should return false when insertion is not acknowledged`() = runTest {
        // given
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns false
        coEvery { taskCollection.insertOne(any<TaskDto>(), any()) } returns insertResult
        // When
        val result = mongoTaskDataSource.addTask(sampleTaskDto)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `getTasks should return list of tasks from collection`() = runTest {
        // Given
        val tasks = listOf(validTaskDto, validTaskDto)
        val findFlow = mockk<FindFlow<TaskDto>>(relaxed = true)
        coEvery { taskCollection.find() } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<TaskDto>>()
            tasks.forEach { collector.emit(it) }
        }

        // When
        val result = mongoTaskDataSource.getTasks()

        // then
        coVerify { taskCollection.find() }
        assertThat(result).containsExactlyElementsIn(tasks)
    }

    @Test
    fun `getTaskById should return task when it exists`() = runTest {
        // given
        val findFlow = mockk<FindFlow<TaskDto>>(relaxed = true)
        coEvery { taskCollection.find(any<Bson>()) } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<TaskDto>>()
            collector.emit(sampleTaskDto)
        }

        // When
        val result = mongoTaskDataSource.getTaskById(sampleTaskDto.id)

        // Then
        assertThat(result).isEqualTo(sampleTaskDto)
    }

    @Test
    fun `getTaskById should return null when it is not exists`() = runTest {
        // given
        val findFlow = mockk<FindFlow<TaskDto>>(relaxed = true)
        coEvery { taskCollection.find(any<Bson>()) } returns findFlow


        // When
        val result = mongoTaskDataSource.getTaskById(sampleTaskDto.id)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `deleteTask should return true when deletion is acknowledged`() = runTest {
        // given
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns true
        coEvery { taskCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        // When
        val result = mongoTaskDataSource.deleteTask(sampleTaskDto.id)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `deleteTask should return false when deletion is not acknowledged`() = runTest {
        // given
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns false
        coEvery { taskCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        // When
        val result = mongoTaskDataSource.deleteTask(sampleTaskDto.id)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `updateTask should return true when update is acknowledged`() = runTest {
        // given
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns true
        coEvery {
            taskCollection.updateOne(
                any<Bson>(),
                any<Bson>(),
                any()
            )
        } returns updateResult


        // when
        val result = mongoTaskDataSource.updateTask(sampleTaskDto)
        val expectedFilter = Filters.eq(TaskDto::id.name, sampleTaskDto.id)
        val expectedUpdates = Updates.combine(
            Updates.set(TaskDto::title.name, sampleTaskDto.title),
            Updates.set(TaskDto::description.name, sampleTaskDto.description),
            Updates.set(TaskDto::projectId.name, sampleTaskDto.projectId),
            Updates.set(TaskDto::stateId.name, sampleTaskDto.stateId),
            Updates.set(TaskDto::assignedTo.name, sampleTaskDto.assignedTo),
            Updates.set(TaskDto::createdBy.name, sampleTaskDto.createdBy),
            Updates.set(TaskDto::createdAt.name, sampleTaskDto.createdAt),
            Updates.set(TaskDto::updatedAt.name, sampleTaskDto.updatedAt),
        )
        // then
        coVerify {
            taskCollection.updateOne(
                match<Bson> { it == expectedFilter },
                match<Bson> { it == expectedUpdates },
                any<UpdateOptions>()
            )
        }
        assertThat(result).isTrue()
    }

    @Test
    fun `updateTask should return false when update is not acknowledged`() = runTest {
        // given
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns false
        coEvery {
            taskCollection.updateOne(
                any<Bson>(),
                any<Bson>(),
                any()
            )
        } returns updateResult


        // when
        val result = mongoTaskDataSource.updateTask(sampleTaskDto)
        val expectedFilter = Filters.eq(TaskDto::id.name, sampleTaskDto.id)
        val expectedUpdates = Updates.combine(
            Updates.set(TaskDto::title.name, sampleTaskDto.title),
            Updates.set(TaskDto::description.name, sampleTaskDto.description),
            Updates.set(TaskDto::projectId.name, sampleTaskDto.projectId),
            Updates.set(TaskDto::stateId.name, sampleTaskDto.stateId),
            Updates.set(TaskDto::assignedTo.name, sampleTaskDto.assignedTo),
            Updates.set(TaskDto::createdBy.name, sampleTaskDto.createdBy),
            Updates.set(TaskDto::createdAt.name, sampleTaskDto.createdAt),
            Updates.set(TaskDto::updatedAt.name, sampleTaskDto.updatedAt),
        )
        // then
        coVerify {
            taskCollection.updateOne(
                match<Bson> { it == expectedFilter },
                match<Bson> { it == expectedUpdates },
                any<UpdateOptions>()
            )
        }
        assertThat(result).isFalse()
    }
}