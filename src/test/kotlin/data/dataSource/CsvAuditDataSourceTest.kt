package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.createDummyAudits
import data.dataSource.util.CsvHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvAuditDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler(CsvWriter(), CsvReader())
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
    fun `should return all audits from file`() {
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyProjectCreateAction
        )
        //when
        dataSource.addAudit(expectedAudits[0])
        dataSource.addAudit(expectedAudits[1])
        val result = dataSource.getAllAudit()
        //then
        assertThat(result).isEqualTo(expectedAudits)
    }


}