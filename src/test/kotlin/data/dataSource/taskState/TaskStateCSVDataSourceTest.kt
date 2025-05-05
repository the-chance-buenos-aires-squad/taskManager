package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.*
import kotlin.test.Test

class TaskStateCSVDataSourceTest {

    private var testStateFile: File = File.createTempFile("test_states", "csv")
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskStateCSVDataSource: TaskStateCSVDataSource


    private val id1 = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    private val csvRows = listOf(
        listOf(id1.toString(), "To Do", "P001"),
        listOf(id2.toString(), "In Progress", "P002")
    )

    @BeforeEach
    fun setup() {
        csvHandler = mockk()
        taskStateCSVDataSource = TaskStateCSVDataSource(testStateFile, csvHandler)
    }

    @Test
    fun `should return true when state is created successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(listOf(UUID.randomUUID().toString(), "blocked", "P005"))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state already exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(listOf(id1.toString(), "To Do", "P001"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is edited successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(listOf(id2.toString(), "Review", "P002"))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to edit non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(listOf(UUID.randomUUID().toString(), "Blocked", "P007"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState(id1)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to delete non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState(UUID.randomUUID())

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

        val result = taskStateCSVDataSource.existsTaskState("To Do", "P001")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state does not exist`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("Nonexistent", "P999")

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when name is correct but projectId is wrong`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("To Do", "P999") // name matches, projectId doesn't

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when name is wrong but projectId is correct`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("Nonexistent", "P001") // name doesn't match, projectId matches

        assertThat(result).isFalse()
    }

}