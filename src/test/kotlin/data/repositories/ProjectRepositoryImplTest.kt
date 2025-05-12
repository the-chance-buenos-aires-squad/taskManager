package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProjectRepositoryImplTest {
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var projectDtoMapper: ProjectDtoMapper
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    val id: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        projectDtoMapper = mockk(relaxed = true)
        projectDataSource = mockk(relaxed = true)
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource, projectDtoMapper)
    }

    @Test
    fun `should return true if project created successfully`() = runTest {
        coEvery { projectDataSource.addProject(any()) } returns true

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not created successfully`() = runTest {
        coEvery { projectDataSource.addProject(any()) } returns false

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() = runTest {
        coEvery { projectDataSource.updateProject(any()) } returns true

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not updated successfully`() = runTest {
        coEvery { projectDataSource.updateProject(any()) } returns false

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project deleted successfully`() = runTest {
        coEvery { projectDataSource.deleteProject(any()) } returns true

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not deleted successfully`() = runTest {
        coEvery { projectDataSource.deleteProject(any()) } returns false

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isFalse()
    }


    @Test
    fun `should return list of projects if there are projects`() = runTest {
        val testDto = ProjectDto(
            id.toString(),
            "ahmed",
            "ahmed mate",
            LocalDateTime.now().toString()
        )
        coEvery { projectDataSource.getAllProjects() } returns listOf(testDto, testDto)

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).hasSize(2)
        verify { projectDtoMapper.toEntity(testDto) }
    }

    @Test
    fun `should return empty list if there aren't projects`() = runTest {
        coEvery { projectDataSource.getAllProjects() } returns emptyList()

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).isEmpty()
    }
}