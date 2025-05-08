package data.dataSource.project

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.ProjectDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvProjectDataSourceTest {
    private lateinit var testFile: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var csvReader: CsvReader
    private lateinit var csvProjectDataSource: CsvProjectDataSource
    private lateinit var projectDtoParser: ProjectDtoParser
    private lateinit var fileMock:File

    val now = LocalDateTime.now().toString()
    val id: UUID = UUID.randomUUID()
    private val projectDto = ProjectDto(id.toString(), "ahmed", "ahmed mohamed egypt", now)
    private val projectDto2 = ProjectDto(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", now)

    private val rawProject = listOf(projectDto.id, projectDto.name, projectDto.description, projectDto.createdAt)
    private val rawProject2 = listOf(projectDto2.id, projectDto2.name, projectDto2.description, projectDto2.createdAt)

    @BeforeEach
    fun setup() {
        projectDtoParser = mockk(relaxed = true)
        csvReader = mockk(relaxed = true)
        csvHandler = CsvHandler(csvReader)
        fileMock = mockk(relaxed = true)
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile, projectDtoParser, csvHandler)
    }

    @Test
    fun `should return true when project created`() = runTest {
        val result = csvProjectDataSource.addProject(projectDto)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return project if project found`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject)
        every { projectDtoParser.toDto(rawProject) } returns projectDto

        val projectIsFound = csvProjectDataSource.getProjectById(UUID.fromString(projectDto.id))

        assertThat(projectIsFound).isEqualTo(projectDto)
    }

    @Test
    fun `should return null if project not found`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject2)
        every { projectDtoParser.toDto(rawProject) } returns projectDto2

        val projectIsFound = csvProjectDataSource.getProjectById(UUID.randomUUID())

        assertThat(projectIsFound).isNull()
    }

    @Test
    fun `should return true when project deleted`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject)
        every { projectDtoParser.toDto(rawProject) } returns projectDto

        val isProjectDeleted = csvProjectDataSource.deleteProject(UUID.fromString(projectDto.id))

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return false when project not found`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject2)
        every { projectDtoParser.toDto(rawProject) } returns projectDto2

        val isProjectDeleted = csvProjectDataSource.deleteProject(UUID.randomUUID())

        assertThat(isProjectDeleted).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject, rawProject2)
        every { projectDtoParser.toDto(rawProject) } returns projectDto
        every { projectDtoParser.toDto(rawProject2) } returns projectDto2

        val isProjectUpdated = csvProjectDataSource.updateProject(projectDto)
        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return false if project not found`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject)
        every { projectDtoParser.toDto(rawProject) } returns projectDto

        val isProjectUpdated = csvProjectDataSource.updateProject(projectDto2)

        assertThat(isProjectUpdated).isFalse()
    }

    @Test
    fun `should return emptyList of projects`() = runTest {
        every { fileMock.exists() } returns true
        every { csvHandler.read(fileMock) } returns emptyList()
        val dataSource = CsvProjectDataSource(fileMock, projectDtoParser, csvHandler)

        val all = dataSource.deleteProject(UUID.randomUUID())

        assertThat(all).isFalse()
    }

    @Test
    fun `should return list of projects`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(rawProject)
        every { projectDtoParser.toDto(rawProject) } returns projectDto
        val all = csvProjectDataSource.getAllProjects()
        assertThat(all).hasSize(1)
    }
}
