package data.dataSource.audit

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.DummyTaskAuditRow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvAuditDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler(CsvReader())
    private lateinit var dataSource: CsvAuditDataSource
    private val auditDtoParser = AuditDtoParser()

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
    fun `first line in the file is exactly equal to the first added audit`() = runTest {
        //given
        val expectedRow = DummyTaskAuditRow

        //when
        dataSource.addAudit(DummyTaskAuditDto)

        //then
        val resultFirstRow = file.readLines()[0].split(",")
        assertThat(resultFirstRow).isEqualTo(expectedRow)
    }


    @Test
    fun `should return all audits from file`() = runTest {

        //when
        dataSource.addAudit(DummyTaskAuditDto)
        dataSource.addAudit(DummyTaskAuditDto)
        val result = dataSource.getAllAudit()
        //then
        assertThat(result).isEqualTo(listOf(DummyTaskAuditDto, DummyTaskAuditDto))
    }


}