package data.dataSource.audit

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.DummyTaskAuditRow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvAuditDataSourceTest {

    private lateinit var file: File
    private val csvHandler: CsvHandler = mockk(relaxed = true)
    private lateinit var dataSource: CsvAuditDataSource
    private val auditDtoParser: AuditDtoParser = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("audit_test", ".csv")
        file.writeText("")
        dataSource = CsvAuditDataSource(csvHandler, auditDtoParser, file)
    }

    @Test
    fun `getAllAudit should return empty list when no audits exist`() = runTest {
        //when
        val result = dataSource.getAllAudit()
        //then
        assertThat(result).isEmpty()
    }


    @Test
    fun `should return true when add audit`() = runTest {
        //when
        val result = dataSource.addAudit(DummyTaskAuditDto)
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `getAllAudit should return parsed audit list when csv has data`() = runTest {
        // Arrange
        every { csvHandler.read(file) } returns listOf(DummyTaskAuditRow)

        // Act
        val result = dataSource.getAllAudit()

        // Assert
        assertThat(result).hasSize(1)
    }

    @Test
    fun `addAudit should write parsed row using CsvHandler`() = runTest {
        // Arrange
        val expectedRow = auditDtoParser.toType(DummyTaskAuditDto)
        every { csvHandler.write(expectedRow, file, true) } returns Unit

        // Act
        val result = dataSource.addAudit(DummyTaskAuditDto)

        // Assert
        assertThat(result).isTrue()
        verify { csvHandler.write(expectedRow, file, true) }
    }

    @Test
    fun `getAllAudit should return empty list if CsvHandler returns empty list`() = runTest {
        // Arrange
        every { csvHandler.read(file) } returns emptyList()

        // Act
        val result = dataSource.getAllAudit()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `addAudit should succeed even if write is called multiple times`() = runTest {
        // Arrange
        every { csvHandler.write(any(), file, true) } returns Unit

        // Act
        repeat(3) {
            val result = dataSource.addAudit(DummyTaskAuditDto)
            assertThat(result).isTrue()
        }

        // Assert
        verify(exactly = 3) { csvHandler.write(any(), file, true) }
    }
}
