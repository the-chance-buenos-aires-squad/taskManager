package data.dataSource.task

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CsvTaskDataSourceTest {

    private lateinit var file: File
    private lateinit var dataSource: CsvTaskDataSource
    private lateinit var csvHandler: CsvHandler

    @BeforeEach
    fun setup() {
        file = File.createTempFile("task_test", ".csv").apply {
            writeText("")
            deleteOnExit()
        }
        csvHandler = CsvHandler(CsvReader())
        dataSource = CsvTaskDataSource(csvHandler, file)
    }

    @Test
    fun `addTask should write row and return true`() {
        val taskRow = generateTaskRow()
        val result = dataSource.addTask(taskRow)
        assertThat(result).isTrue()
        assertThat(dataSource.getTasks()).contains(taskRow)
    }

    @Test
    fun `getTasks should return all written tasks`() {
        val taskRow1 = generateTaskRow()
        val taskRow2 = generateTaskRow()
        dataSource.addTask(taskRow1)
        dataSource.addTask(taskRow2)
        val tasks = dataSource.getTasks()
        assertThat(tasks).hasSize(2)
    }

    @Test
    fun `getTaskById should return correct row`() {
        val taskRow = generateTaskRow()
        dataSource.addTask(taskRow)
        val fetched = dataSource.getTaskById(taskRow[0])
        assertThat(fetched).isEqualTo(taskRow)
    }

    @Test
    fun `updateTask should return false if id not found`() {
        val taskRow = generateTaskRow()
        val result = dataSource.updateTask(taskRow)
        assertThat(result).isFalse()
    }

    @Test
    fun `updateTask should modify row and return true`() {
        val original = generateTaskRow()
        dataSource.addTask(original)
        val updated = original.toMutableList().apply { set(1, "Updated Title") }
        val result = dataSource.updateTask(updated)
        assertThat(result).isTrue()
        val fetched = dataSource.getTaskById(updated[0])
        assertThat(fetched).isEqualTo(updated)
    }

    @Test
    fun `deleteTask should return false if id not found`() {
        val result = dataSource.deleteTask(UUID.randomUUID().toString())
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteTask should remove row and return true`() {
        val row = generateTaskRow()
        dataSource.addTask(row)
        val result = dataSource.deleteTask(row[0])
        assertThat(result).isTrue()
        assertThat(dataSource.getTasks()).doesNotContain(row)
    }

    @Test
    fun `addTask should return false and print message if write fails`() {
        // Given
        val mockHandler = mockk<CsvHandler>()
        val dummyFile = File.createTempFile("failing", ".csv")
        val taskRow = listOf("id", "title", "desc", "proj", "state", "", "creator", "now", "now")

        every { mockHandler.write(any(), any(), any()) } throws RuntimeException("Simulated failure")

        val dataSource = CsvTaskDataSource(mockHandler, dummyFile)

        // When
        val result = dataSource.addTask(taskRow)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `updateTask should return false and print message if write fails`() {
        // Given
        val mockHandler = mockk<CsvHandler>()
        val tempFile = File.createTempFile("test_tasks", ".csv").apply { deleteOnExit() }

        val taskRow = listOf("123", "title", "desc", "proj", "state", "", "creator", "now", "now")

        // Pretend that csvHandler.read works and returns an existing matching task
        every { mockHandler.read(any()) } returns listOf(taskRow)
        // Simulate failure on write
        every { mockHandler.write(any(), any(), any()) } throws RuntimeException("Simulated failure")

        val dataSource = CsvTaskDataSource(mockHandler, tempFile)

        // When
        val result = dataSource.updateTask(taskRow)

        // Then
        assertThat(result).isFalse()
    }


    private fun generateTaskRow(): List<String> {
        return listOf(
            UUID.randomUUID().toString(),
            "Test Task",
            "Description",
            "project-123",
            "todo",
            "user123",
            "creator123",
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString()
        )
    }
}