package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import domain.entities.Audit
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

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






}