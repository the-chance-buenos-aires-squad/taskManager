package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.ProjectDataSource
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectRepositoryImplTest {
    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl

    @BeforeEach
    fun setup() {
        projectDataSource = mockk(relaxed = true)
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `should return true if project created successfully`() {
        every { projectDataSource.saveData(any()) } returns true

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not created successfully`() {
        every { projectDataSource.saveData(any()) } returns false

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() {
        every { projectDataSource.updateProject(any()) } returns true

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not updated successfully`() {
        every { projectDataSource.updateProject(any()) } returns false

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true if project deleted successfully`() {
        every { projectDataSource.deleteData(any()) } returns true

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project not deleted successfully`() {
        every { projectDataSource.deleteData(any()) } returns false

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return project if project exist`() {
        every { projectDataSource.findProjectById(any()) } returns createDummyProject()

        val result = projectRepositoryImpl.findProjectById(createDummyProject().id)

        assertThat(result).isNotNull()
    }

    @Test
    fun `should return null if project not exist`() {
        every { projectDataSource.findProjectById(any()) } returns null

        val result = projectRepositoryImpl.findProjectById(createDummyProject().id)

        assertThat(result).isNull()
    }

    @Test
    fun `should return list of projects if there are projects`() {
        every { projectDataSource.getAllProjects() } returns listOf(createDummyProject())

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).hasSize(1)
    }

    @Test
    fun `should return empty list if there aren't projects`() {
        every { projectDataSource.getAllProjects() } returns emptyList()

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).isEmpty()
    }
}