package data.dataSource.project

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.ProjectDto
import dummyData.DummyProjects
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
    private val csvHandler: CsvHandler= mockk(relaxed = true)
    private lateinit var csvProjectDataSource: CsvProjectDataSource
    private val projectDtoParser: ProjectDtoParser= mockk(relaxed = true)
    private lateinit var fileMock: File


    @BeforeEach
    fun setup() {
        fileMock = mockk(relaxed = true)
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile, projectDtoParser, csvHandler)
    }

    @Test
    fun `should return true when project created`() = runTest {
        val result = csvProjectDataSource.addProject(DummyProjects.projectDto1)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return project if project found`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject1)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto1

        val projectIsFound = csvProjectDataSource.getProjectById(DummyProjects.projectDto1._id)

        assertThat(projectIsFound).isEqualTo(DummyProjects.projectDto1)
    }

    @Test
    fun `should return null if project not found when call get project by id function`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject2)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto2

        val projectIsFound = csvProjectDataSource.getProjectById(UUID.randomUUID().toString())

        assertThat(projectIsFound).isNull()
    }

    @Test
    fun `should return true when project deleted`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject1)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto1

        val isProjectDeleted = csvProjectDataSource.deleteProject(DummyProjects.projectDto1._id)

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return false if project not found when call delete function`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject2)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto2

        val isProjectDeleted = csvProjectDataSource.deleteProject(UUID.randomUUID().toString())

        assertThat(isProjectDeleted).isFalse()
    }

    @Test
    fun `should return true if project updated successfully`() = runTest {
        every { csvHandler.read(testFile) } returns DummyProjects.rowList
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto1
        every { projectDtoParser.toDto(DummyProjects.rawProject2) } returns DummyProjects.projectDto2

        val isProjectUpdated = csvProjectDataSource.updateProject(DummyProjects.projectDto1)

        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return false if project not found when call update function`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject1)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto1

        val isProjectUpdated = csvProjectDataSource.updateProject(DummyProjects.projectDto2)

        assertThat(isProjectUpdated).isFalse()
    }

    @Test
    fun `should return emptyList of projects`() = runTest {
        every { fileMock.exists() } returns true
        every { csvHandler.read(fileMock) } returns emptyList()

        val dataSource = CsvProjectDataSource(fileMock, projectDtoParser, csvHandler)
        val all = dataSource.deleteProject(UUID.randomUUID().toString())

        assertThat(all).isFalse()
    }

    @Test
    fun `should return list of projects`() = runTest {
        every { csvHandler.read(testFile) } returns listOf(DummyProjects.rawProject1)
        every { projectDtoParser.toDto(DummyProjects.rawProject1) } returns DummyProjects.projectDto1

        val all = csvProjectDataSource.getAllProjects()

        assertThat(all).hasSize(1)
    }
}
