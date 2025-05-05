package data.dataSource.util


import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvHandlerTest {

    private lateinit var testTempFile: File
    private lateinit var csvHandler: CsvHandler

    @BeforeEach
    fun setUp() {
        testTempFile = File.createTempFile("csv_test", ".csv")
        testTempFile.deleteOnExit()
        csvHandler = CsvHandler(CsvReader())
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

    @Test
    fun `writeHeaderIfNotExist should overwrite file`() {
        val headerRow = listOf("header1", "header2")

        // First write
        csvHandler.writeHeaderIfNotExist(headerRow, testTempFile)

        // Read back
        val content = csvHandler.read(testTempFile)
        assertThat(content[0]).isEqualTo(headerRow)
    }

    @Test
    fun `writing with append true does not override the header row`() {
        //given
        val row = listOf("test1", "test2")
        val headerRow = listOf("header1", "header2")

        //when
        csvHandler.writeHeaderIfNotExist(headerRow, testTempFile)
        csvHandler.write(row = row, file = testTempFile, append = true)


        //then
        val content = csvHandler.read(testTempFile)
        assertThat(content[0]).isEqualTo(headerRow)
        assertThat(content[1]).isEqualTo(row)
    }


}