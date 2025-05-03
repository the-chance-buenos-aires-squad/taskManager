package data.dataSource.project

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.repositories.mappers.ProjectMapper
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvProjectDataSourceTest {
    private lateinit var testFile: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var csvWriter: CsvWriter
    private lateinit var csvReader: CsvReader
    private lateinit var csvProjectDataSource: CsvProjectDataSource

    val id: UUID = UUID.randomUUID()
    val project = listOf(id.toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project2 =
        listOf(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())

    @BeforeEach
    fun setup() {
        csvWriter = mockk(relaxed = true)
        csvReader = mockk(relaxed = true)
        csvHandler = CsvHandler(csvWriter, csvReader)
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile, csvHandler)
    }

    @Test
    fun `should return true when project created`() {
        val result = csvProjectDataSource.addProject(project)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return project if project found`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val projectIsFound = csvProjectDataSource.getProjectById(id)

        assertThat(projectIsFound).isNotNull()
    }

    @Test
    fun `should return null if project not found`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val projectIsFound = csvProjectDataSource.getProjectById(UUID.randomUUID())

        assertThat(projectIsFound).isNull()
    }

    @Test
    fun `should return true when project deleted`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectDeleted = csvProjectDataSource.deleteProject(id)

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return false when project not found`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectDeleted = csvProjectDataSource.deleteProject(UUID.randomUUID())

        assertThat(isProjectDeleted).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectUpdated = csvProjectDataSource.updateProject(project)

        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return false if project not found`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        val isProjectUpdated = csvProjectDataSource.updateProject(project2)

        assertThat(isProjectUpdated).isFalse()
    }

    @Test
    fun `should return list of projects`() {
        every { csvHandler.read(testFile) } returns listOf(project)

        assertThat(csvProjectDataSource.getAllProjects()).hasSize(1)
    }

    @Test
    fun `should return null when file is empty`() {
        every { csvHandler.read(testFile) } returns emptyList()

        assertThat(csvProjectDataSource.getAllProjects()).isEmpty()
    }
}