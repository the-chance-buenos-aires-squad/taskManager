package data.dataSource

import com.google.common.truth.Truth.assertThat
import dummyData.createDummyProject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvProjectDataSourceTest {
    private lateinit var testFile: File
    private lateinit var csvProjectDataSource: CsvProjectDataSource
    private val project = createDummyProject()

    @BeforeEach
    fun setup() {
        testFile = File.createTempFile("testFile", ".csv")
        csvProjectDataSource = CsvProjectDataSource(testFile)
    }

    @Test
    fun `should return true when project saved`() {
        val result = csvProjectDataSource.saveData(project)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return project if exist`() {
        csvProjectDataSource.saveData(project)
        val projectIsFound = csvProjectDataSource.findProjectById(project.id)

        assertThat(projectIsFound).isNotNull()
    }

    @Test
    fun `should return true when project deleted`() {
        csvProjectDataSource.saveData(project)
        val isProjectDeleted = csvProjectDataSource.deleteData(project.id)

        assertThat(isProjectDeleted).isTrue()
    }

    @Test
    fun `should return true if project updated successfully`() {
        csvProjectDataSource.saveData(project)
        val isProjectUpdated = csvProjectDataSource.updateProject(createDummyProject("1"))

        assertThat(isProjectUpdated).isTrue()
    }

    @Test
    fun `should return list of projects`() {
        csvProjectDataSource.saveData(project)
        csvProjectDataSource.saveData(project)

        assertThat(csvProjectDataSource.getAllProjects()).hasSize(2)
    }
}