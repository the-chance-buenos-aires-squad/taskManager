package data.dataSource

import com.google.common.truth.Truth.assertThat
import data.util.CsvHandler
import domain.entities.State
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test

class TaskStateCSVDataSourceTest {

    private var testStateFile: File = File("test_states.csv")
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskStateCSVDataSource: TaskStateCSVDataSource

    private val csvRows = listOf(
        listOf("1", "To Do", "P001"),
        listOf("2", "In Progress", "P002")
    )

    @BeforeEach
    fun setup() {
        taskStateCSVDataSource = TaskStateCSVDataSource(testStateFile, csvHandler)
    }

    @Test
    fun `should return true when state is created successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(State("5", "blocked", "P005"))

        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when state already exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.createTaskState(State("1", "To Do", "P001"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is edited successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(State("2", "Review", "P002"))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to edit non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(State("7", "Blocked", "P007"))

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState("1")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to delete non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.deleteTaskState("9")

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

        val result = taskStateCSVDataSource.existsTaskState("1")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state does not exist`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("99")

        assertThat(result).isFalse()
    }

}