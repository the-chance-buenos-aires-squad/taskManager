package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvProjectDataSourceTest {
    private lateinit var testFile: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var csvWriter: CsvWriter
    private lateinit var csvReader: CsvReader
    private lateinit var csvProjectDataSource: CsvProjectDataSource
    private val project = createDummyProject()
    private val csvRows = listOf(
        listOf("id", "name", "description", "createdAt"), // header
        listOf("1", "Project 1", "Description 1", "2024-04-29T12:00"),
        listOf("2", "Project 1", "Description 1", "2024-04-29T12:00")
    )
    private val header = 0

    @BeforeEach
    fun setup() {
        csvWriter = mockk(relaxed = true)
        csvReader = mockk(relaxed = true)
        csvHandler = CsvHandler(csvWriter, csvReader)
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile, csvHandler)
    }

    @Test
    fun `should return true when project saved`() {
        val result = csvProjectDataSource.saveData(project)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when project exist`() {
        every { csvHandler.read(testFile) } returns csvRows

        val result = csvProjectDataSource.saveData(createDummyProject("1"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return project if project found`() {
        every { csvHandler.read(testFile) } returns csvRows

        val projectIsFound = csvProjectDataSource.findProjectById("1")

        assertThat(projectIsFound).isNotNull()
    }

    @Test
    fun `should return null if project not found`() {
        every { csvHandler.read(testFile) } returns csvRows

        val projectIsFound = csvProjectDataSource.findProjectById("3")

        assertThat(projectIsFound).isNull()
    }

    @Test
    fun `should return true when project deleted`() {
        every { csvHandler.read(testFile) } returns csvRows

        val isProjectDeleted = csvProjectDataSource.deleteData("1")

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return false when project not found`() {
        every { csvHandler.read(testFile) } returns csvRows

        val isProjectDeleted = csvProjectDataSource.deleteData("3")

        assertThat(isProjectDeleted).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() {
        every { csvHandler.read(testFile) } returns csvRows

        val isProjectUpdated = csvProjectDataSource.updateProject(createDummyProject("1"))

        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return false if project not found`() {
        every { csvHandler.read(testFile) } returns csvRows

        val isProjectUpdated = csvProjectDataSource.updateProject(createDummyProject("3"))

        assertThat(isProjectUpdated).isFalse()
    }

    @Test
    fun `should return list of projects`() {
        every { csvHandler.read(testFile) } returns csvRows

        assertThat(csvProjectDataSource.getAllProjects()).hasSize(2)
    }

    @Test
    fun `should return null when file is empty`() {
        every { csvHandler.read(testFile) } returns listOf(csvRows[header])

        assertThat(csvProjectDataSource.getAllProjects()).isEmpty()
    }
}