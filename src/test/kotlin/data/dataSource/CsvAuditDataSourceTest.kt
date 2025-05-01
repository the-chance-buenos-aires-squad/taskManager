package data.dataSource

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import domain.entities.Audit
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvAuditDataSourceTest {

    private val file = File("src/main/kotlin/data/resource/audit.csv")
    private val csvHandler = CsvHandler(CsvWriter(), CsvReader())
    private val dataSource = CsvAuditDataSource(csvHandler, file)

    @AfterEach
    fun cleanUp(){

    }






}