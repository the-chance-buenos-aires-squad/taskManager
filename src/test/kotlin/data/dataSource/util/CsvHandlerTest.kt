package data.dataSource.util


import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvHandlerTest {

    private lateinit var file: File
    private lateinit var handler: CsvHandler
    private lateinit var csvReader: CsvReader

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("csv_test", ".csv")
        file.writeText("") // clear it
        csvReader = CsvReader() // use real reader for integration-style test
        handler = CsvHandler(csvReader)
    }

    @Test
    fun `write should add row to CSV`() {
        val row = listOf("id", "name", "email")
        handler.write(row, file)

        val lines = file.readLines()
        assertThat(lines.first()).contains("id,name,email")
    }

    @Test
    fun `writeHeaderIfNotExist should write header`() {
        val header = listOf("ID", "Username", "Password")
        handler.writeHeaderIfNotExist(header, file)

        val content = file.readLines().first()
        assertThat(content).isEqualTo("ID,Username,Password")
    }

    @Test
    fun `read should return written rows`() {
        val row = listOf("1", "Ahmed", "ahmed@test.com")
        handler.write(row, file)

        val rows = handler.read(file)
        assertThat(rows.first()).isEqualTo(row)
    }

    @Test
    fun `read should return empty list when file is empty`() {
        val result = handler.read(file)
        assertThat(result).isEmpty()
    }
}