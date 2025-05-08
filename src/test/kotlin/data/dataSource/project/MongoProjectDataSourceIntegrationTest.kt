package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import data.dto.ProjectDto
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.bson.UuidRepresentation
import org.junit.jupiter.api.*
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoProjectDataSourceIntegrationTest {

    private lateinit var container: MongoDBContainer
    private lateinit var mongoDataSource: MongoProjectDataSource
    private lateinit var projectDto: ProjectDto

    @BeforeAll
    fun setupContainer() {
        container = MongoDBContainer(DockerImageName.parse("mongo:6.0"))
        container.start()

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(container.connectionString))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        val client = MongoClient.create(settings)
        val database = client.getDatabase("test-db")

        mongoDataSource = MongoProjectDataSource(database)
    }
    @BeforeEach
    fun setup(){
        projectDto = ProjectDto(
            _id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "Integration test project",
            createdAt = LocalDateTime.now().toString()
        )
    }
    @AfterEach
    fun clearDatabase() = runTest {
        mongoDataSource.getAllProjects().forEach {
            mongoDataSource.deleteProject(it._id)
        }
    }

    @AfterAll
    fun tearDown() {
        container.stop()
    }

    @Test
    fun `should add and retrieve project`() = runTest {
        val added = mongoDataSource.addProject(projectDto)
        assertThat(added).isTrue()

    }

    @Test
    fun `should return all projects`() = runTest {
        mongoDataSource.addProject(projectDto)
        val result = mongoDataSource.getAllProjects()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should update existing project`() = runTest {
        val updated = projectDto.copy(name = "Updated")
        val result = mongoDataSource.updateProject(updated)
        assertThat(result).isTrue()

    }

    @Test
    fun `should delete project`() = runTest {
        mongoDataSource.addProject(projectDto)
        val deleted = mongoDataSource.deleteProject(projectDto._id)
        assertThat(deleted).isTrue()

    }

    @Test
    fun `should return null if project not found by id`() = runTest {
        val result = mongoDataSource.getProjectById(UUID.randomUUID().toString())
        assertThat(result).isNull()
    }

    @Test
    fun `should return project by id if project found`() = runTest {
        mongoDataSource.addProject(projectDto)
        val result = mongoDataSource.getProjectById(projectDto._id)
        assertThat(result).isNotNull()
    }
}
