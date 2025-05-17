package data.dataSource.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.TaskDto
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvTaskDataSourceTest {

    private lateinit var file: File
    private val csvHandler: CsvHandler = mockk(relaxed = true)
    private val parser: TaskDtoParser = mockk(relaxed = true)
    private lateinit var dataSource: CsvTaskDataSource
    private val taskDto = TaskDto("1", "title", "desc", "pId", "sId", "uId", "createdBy", "2024-05-01T10:00", "2024-05-01T11:00")

    private val row = listOf("1", "title", "desc", "pId", "sId", "uId", "createdBy", "2024-05-01T10:00", "2024-05-01T11:00")



    @BeforeEach
    fun setup() {
        file = File.createTempFile("task_test", ".csv")
        file.writeText("")
        dataSource = CsvTaskDataSource(csvHandler = csvHandler, taskDtoParser = parser, file = file)


    }

    @Test
    fun `addTask should return true when write succeeds`() = runTest {
        every { parser.toType(taskDto) } returns row

        val result = dataSource.addTask(taskDto)

        assertThat(result).isTrue()
    }

    @Test
    fun `getTasks should return parsed list from csv`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto

        val result = dataSource.getTasks()

        assertThat(result).containsExactly(taskDto)
    }

    @Test
    fun `getTaskById should return correct task when found`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto

        val result = dataSource.getTaskById("1")

        assertThat(result).isEqualTo(taskDto)
    }

    @Test
    fun `getTaskById should return null when not found`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto

        val result = dataSource.getTaskById("non-existent")

        assertThat(result).isNull()
    }

    @Test
    fun `updateTask should return true when task exists`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto
        every { parser.toType(any()) } returns row

        val result = dataSource.updateTask(taskDto)

        assertThat(result).isTrue()
    }

    @Test
    fun `updateTask should return false when task not exists`() = runTest {
        every { csvHandler.read(file) } returns emptyList()

        val result = dataSource.updateTask(taskDto)

        assertThat(result).isFalse()
    }

    @Test
    fun `deleteTask should return true when task is removed`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto
        every { parser.toType(taskDto) } returns row

        val result = dataSource.deleteTask("1")

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteTask should return false when task not found`() = runTest {
        every { csvHandler.read(file) } returns listOf(row)
        every { parser.fromType(row) } returns taskDto

        val result = dataSource.deleteTask("non-existent")

        assertThat(result).isFalse()
    }
}