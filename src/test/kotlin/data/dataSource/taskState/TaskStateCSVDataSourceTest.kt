package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.*
import kotlin.test.Test

class TaskStateCSVDataSourceTest {

    private lateinit var testStateFile: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var taskStateCSVDataSource: TaskStateCSVDataSource

    private val id1 = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    private val projectId1 = UUID.fromString("10000000-1000-0000-0000-000000000002")
    private val projectId2 = UUID.fromString("10000000-2000-0000-0000-000000000002")


    private val csvRows = listOf(
        listOf(id1.toString(), "To Do", projectId1.toString()),
        listOf(id2.toString(), "In Progress", projectId2.toString())
    )

    @BeforeEach
    fun setup() {
        csvHandler = mockk(relaxed = true)
        testStateFile = File.createTempFile("test_states", "csv")
        taskStateCSVDataSource = TaskStateCSVDataSource(testStateFile, csvHandler)
    }

    @Test
    fun `should return true when state is created successfully`() {
        val result = taskStateCSVDataSource.createTaskState(
            listOf(
                UUID.randomUUID().toString(),
                "blocked",
                UUID.randomUUID().toString()
            )
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `should return true when state is edited successfully`() {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { csvHandler.write(any(), any(), any()) } just Runs

        val result = taskStateCSVDataSource.editTaskState(listOf(id2.toString(), "Review", projectId2.toString()))

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to edit non-existing state`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.editTaskState(
            listOf(
                UUID.randomUUID().toString(),
                "Blocked",
                UUID.randomUUID().toString()
            )
        )

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

        val result = taskStateCSVDataSource.getTaskStates()

        assertThat(result).hasSize(2)
    }


    @Test
    fun `should return true when state exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("To Do", projectId1)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state name exists but projectId is different`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("To Do", projectId2)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when state name is incorrect but projectId exists`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("Done", projectId1)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when state does not exist`() {
        every { csvHandler.read(testStateFile) } returns csvRows

        val result = taskStateCSVDataSource.existsTaskState("Block", UUID.randomUUID())

        assertThat(result).isFalse()
    }

}