package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dummyData.DummyAudits
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

class CsvAuditDataSourceTest {

    private lateinit var  file : File
    private val csvHandler = CsvHandler(CsvWriter(), CsvReader())
    private lateinit var dataSource: CsvAuditDataSource

    @BeforeEach
    fun setUp(){
        file = File.createTempFile("audit_test",".csv")
        file.writeText("")
        dataSource = CsvAuditDataSource(csvHandler, file)
    }


    @Test
    fun `should return true when add audit`(){
        //when
        val result = dataSource.addAudit(DummyAudits.dummyTaskAudit_CreateAction)
        //then
        assertThat(result).isTrue()
    }




}