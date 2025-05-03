package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.audit.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits.dummyProjectCreateActionRow
import data.dataSource.dummyData.createDummyAudits.dummyTaskCreateActionRow
import data.dataSource.util.CsvHandler
import data.dummyData.DummyAudits.DummyTaskAuditRow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvAuditDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler( CsvReader())
    private lateinit var dataSource: CsvAuditDataSource

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("audit_test", ".csv")
        file.writeText("")
        dataSource = CsvAuditDataSource(csvHandler, file)
    }

    @Test
    fun `getAllAudit should return empty list when no audits exist`() {
        //when
        val result = dataSource.getAllAudit()
        //then
        assertThat(result).isEmpty()
    }


    @Test
    fun `should return true when add audit`() {
        //when
        val result = dataSource.addAudit(DummyTaskAuditRow)
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `first line in the file is exactly equal to the first added audit`() {
        //given
        val expectedRow = DummyTaskAuditRow

        //when
        dataSource.addAudit(DummyTaskAuditRow)

        //then
        val resultFirstRow = file.readLines()[0].split(",")
        assertThat(resultFirstRow).isEqualTo(expectedRow)
    }


    @Test
    fun `add audit should return false when changing file writing permission`() {
        //given
        file.setReadOnly()
        //when
        val result = dataSource.addAudit(DummyTaskAuditRow)

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return all audits from file`() {

        //when
        dataSource.addAudit(dummyTaskCreateActionRow)
        dataSource.addAudit(dummyProjectCreateActionRow)
        val result = dataSource.getAllAudit()
        //then
        assertThat(result).isEqualTo(listOf(dummyTaskCreateActionRow, dummyProjectCreateActionRow))
    }


}