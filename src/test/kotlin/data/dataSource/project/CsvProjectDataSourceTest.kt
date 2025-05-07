package data.dataSource.project

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.ProjectDto
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

    val id: UUID = UUID.randomUUID()
    val project = listOf(id.toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val projectDto = ProjectDto(id.toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project2 =
        listOf(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val projectDto2 =
        ProjectDto(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())


    @BeforeEach
    fun setup() {
        projectDtoParser = mockk(relaxed = true)
        csvReader = mockk(relaxed = true)
        csvHandler = CsvHandler(csvReader)
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile, projectDtoParser, csvHandler)
    }

    @Test
    fun `should return true when project created`() = runTest {
        val result = csvProjectDataSource.addProject(projectDto)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return project if project found`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val projectIsFound = csvProjectDataSource.getProjectById(id)

        assertThat(projectIsFound).isNotNull()
    }

    @Test
    fun `should return null if project not found`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val projectIsFound = csvProjectDataSource.getProjectById(UUID.randomUUID())

        assertThat(projectIsFound).isNull()
    }

    @Test
    fun `should return true when project deleted`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectDeleted = csvProjectDataSource.deleteProject(id)

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return false when project not found`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectDeleted = csvProjectDataSource.deleteProject(UUID.randomUUID())

        assertThat(isProjectDeleted).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectUpdated = csvProjectDataSource.updateProject(projectDto)

        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return false if project not found`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectUpdated = csvProjectDataSource.updateProject(projectDto2)

        assertThat(isProjectUpdated).isFalse()
    }

    @Test
    fun `should return list of projects`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project)

        assertThat(csvProjectDataSource.getAllProjects()).hasSize(1)
    }

    @Test
    fun `should return null when file is empty`() = runTest{
        every { csvHandler.read(testFile) } returns emptyList()

        assertThat(csvProjectDataSource.getAllProjects()).isEmpty()
    }

    @Test
    fun `should return update project successfully if user enter valid id`() = runTest{
        every { csvHandler.read(testFile) } returns listOf(project, project2)

        val isProjectUpdated = csvProjectDataSource.updateProject(projectDto)

        assertThat(isProjectUpdated).isTrue()
    }
}