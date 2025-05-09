package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import com.mongodb.MongoException
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.model.Filters
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.ProjectDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.bson.conversions.Bson
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.util.*

class MongoProjectDataSourceIntegrationTest {


    private val projectCollection: MongoCollection<ProjectDto> = mockk(relaxed = true)
    private val mongoDataSource = MongoProjectDataSource(projectCollection)

    private val projectDto = ProjectDto(
        _id = UUID.randomUUID().toString(),
        name = "Test Project",
        description = "Integration test project",
        createdAt = LocalDateTime.now().toString()
    )

    @Test
    fun `should add project when call addProject`() = runTest {
        // given
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns true
        coEvery { projectCollection.insertOne(any<ProjectDto>(), any()) } returns insertResult

        // when
        val result = mongoDataSource.addProject(projectDto)

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when add project is not acknowledged`() = runTest {
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns false
        coEvery { projectCollection.insertOne(any<ProjectDto>(), any()) } returns insertResult

        val result = mongoDataSource.addProject(projectDto)

        assertThat(result).isFalse()
    }

    @Test
    fun `should delete project by id when call deleteProject`() = runTest {
        // given
        val deleteResult = mockk<DeleteResult>()
        coEvery { deleteResult.wasAcknowledged() } returns true
        coEvery { projectCollection.deleteOne(any<Bson>(), any<DeleteOptions>()) } returns deleteResult

        // when
        val result = mongoDataSource.deleteProject(projectDto._id)

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `should get project by id`() = runTest {
        // given
        val findFlow = mockk<FindFlow<ProjectDto>>(relaxed = true)
        coEvery { projectCollection.find(any<Bson>()) } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<ProjectDto>>()
            collector.emit(projectDto)
        }

        // when
        val result = mongoDataSource.getProjectById(projectDto._id)

        // then
        assertThat(result).isEqualTo(projectDto)
    }

    @Test
    fun `should update project when cal`() = runTest {
        // given
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns true
        coEvery {
            projectCollection.updateOne(
                any<Bson>(),
                any<Bson>(),
                any()
            )
        } returns updateResult

        // when
        val result = mongoDataSource.updateProject(projectDto)
        val expectedFilter = Filters.eq(ProjectDto::_id.name, projectDto._id)
        val expectedUpdates = Updates.combine(
            Updates.set(ProjectDto::name.name, projectDto.name),
            Updates.set(ProjectDto::description.name, projectDto.description),
            Updates.set(ProjectDto::createdAt.name, projectDto.createdAt)
        )

        // then
        coVerify {
            projectCollection.updateOne(
                match<Bson> { it == expectedFilter }, // Add <Bson> to match
                match<Bson> { it == expectedUpdates }, // Add <Bson> to match
                any<UpdateOptions>() // Add <UpdateOptions> to any
            )
        }
        assertThat(result).isTrue()
    }


    @Test
    fun `should get all projects`() = runTest {
        // given
        val projects = listOf(
            projectDto,
            projectDto.copy(_id = UUID.randomUUID().toString())
        )
        val findFlow = mockk<FindFlow<ProjectDto>>(relaxed = true)
        coEvery { projectCollection.find() } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<ProjectDto>>()
            projects.forEach { collector.emit(it) }
        }

        // when
        val result = mongoDataSource.getAllProjects()

        // then
        coVerify { projectCollection.find() } // Verify `find()` was called
        assertThat(result).containsExactlyElementsIn(projects) // Check the result
    }

}
