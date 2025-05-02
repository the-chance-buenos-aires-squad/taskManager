package data.dataSource.util

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.*
import java.io.File
import kotlin.test.assertEquals

class CsvHandlerTest {

    private lateinit var testTempFile: File
    private lateinit var csvHandler: CsvHandler

    @BeforeEach
    fun setUp() {
        testTempFile = File.createTempFile("csv_test", ".csv")
        testTempFile.deleteOnExit()
        csvHandler = CsvHandler(CsvWriter(), CsvReader())
    }

    @Test
    fun `write and read CSV content`() {
        val row = listOf("test1", "test2", "test3")

        // Write the row
        csvHandler.write(row, testTempFile, append = false)

        // Read
        val content = csvHandler.read(testTempFile)

        //verify
        assertThat(content.size).isEqualTo(1)
        assertThat(row).isEqualTo(content[0])
    }

}