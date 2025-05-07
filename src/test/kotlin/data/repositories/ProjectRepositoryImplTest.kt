package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.project.ProjectDataSource
import data.dto.ProjectDto
import data.repositories.mappers.ProjectDtoMapper
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class ProjectRepositoryImplTest {
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var csvProjectMapper: ProjectDtoMapper
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    val id: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        csvProjectMapper = mockk(relaxed = true)
        projectDataSource = mockk(relaxed = true)
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource, csvProjectMapper)
    }

    @Test
    fun `should return true if project created successfully`() = runTest {
        coEvery { projectDataSource.addProject(any()) } returns true

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not created successfully`() = runTest{
        coEvery { projectDataSource.addProject(any()) } returns false

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() = runTest{
        coEvery { projectDataSource.updateProject(any()) } returns true

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not updated successfully`() = runTest{
        coEvery { projectDataSource.updateProject(any()) } returns false

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project deleted successfully`() = runTest{
        coEvery { projectDataSource.deleteProject(any()) } returns true

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not deleted successfully`() = runTest{
        coEvery { projectDataSource.deleteProject(any()) } returns false

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return project if project exist`() =runTest{
        coEvery { projectDataSource.getProjectById(any()) } returns ProjectDto(
            id.toString(), "ahmed", "ahmed mate",
            LocalDateTime.now().toString()
        )

        val result = projectRepositoryImpl.getProjectById(createDummyProject().id)

        assertThat(result).isNotNull()
    }

    @Test
    fun `should return null if project not exist`() = runTest{
        coEvery { projectDataSource.getProjectById(any()) } returns null

        val result = projectRepositoryImpl.getProjectById(createDummyProject().id)

        assertThat(result).isNull()
    }

    @Test
    fun `should return list of projects if there are projects`() = runTest{
        coEvery { projectDataSource.getAllProjects() } returns listOf(
            ProjectDto(
                id.toString(),
                "ahmed",
                "ahmed mate",
                LocalDateTime.now().toString()
            )
        )

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).hasSize(1)
    }

    @Test
    fun `should return empty list if there aren't projects`() =runTest{
        coEvery { projectDataSource.getAllProjects() } returns emptyList()

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).isEmpty()
    }
}