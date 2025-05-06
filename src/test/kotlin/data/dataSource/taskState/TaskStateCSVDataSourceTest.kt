package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test

class TaskStateCSVDataSourceTest {

    private var testStateFile: File = File.createTempFile("test_states", "csv")
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskStateCSVDataSource: TaskStateCSVDataSource

    private val csvRows = listOf(
        listOf("10000000-0000-0000-0000-000000000002", "To Do", "10000000-1000-0000-0000-000000000002"),
        listOf("20000000-0000-0000-0000-000000000002", "In Progress", "10000000-2000-0000-0000-000000000002")
    )

    @BeforeEach
    fun setup() {
        csvHandler = mockk()
        taskStateCSVDataSource = TaskStateCSVDataSource(testStateFile, csvHandler)
    }

    @Test
    fun `should return true when state is created successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(listOf("50000000-0000-0000-0000-000000000002", "blocked", "10000000-2000-0000-0000-000000000002"))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state already exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(listOf("10000000-0000-0000-0000-000000000002", "To Do", "10000000-2000-0000-0000-000000000002"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is edited successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(listOf("20000000-0000-0000-0000-000000000002", "Review", "10000000-2000-0000-0000-000000000002"))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to edit non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(listOf("70000000-0000-0000-0000-000000000002", "Blocked", "10000000-7000-0000-0000-000000000002"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState("10000000-0000-0000-0000-000000000002")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to delete non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState("90000000-0000-0000-0000-000000000002")

        assertThat(result).isFalse()
    }

    @Test
    fun `should return all states except header`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.getAllTaskStates()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return true when state exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("10000000-0000-0000-0000-000000000002")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state does not exist`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("990000000-0000-0000-0000-000000000002")

        assertThat(result).isFalse()
    }

}