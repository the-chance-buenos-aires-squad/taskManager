package data.dataSource.taskState

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.TaskStateDto
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.*
import kotlin.test.Test

class CsvTaskStateDataSourceTest {

    private lateinit var testStateFile: File
    private var csvHandler: CsvHandler = mockk(relaxed = true)
    private var taskStateDtoParser: TaskStateDtoParser = mockk(relaxed = true)
    private lateinit var csvTaskStateDataSource: CsvTaskStateDataSource

    private val id1 = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val id2 = UUID.fromString("00000000-0000-0000-0000-000000000002")

    private val projectId1 = UUID.fromString("10000000-1000-0000-0000-000000000002")
    private val projectId2 = UUID.fromString("10000000-2000-0000-0000-000000000002")

    private val stateDto1 = TaskStateDto(id1.toString(), "To Do", projectId1.toString())
    private val stateDto2 = TaskStateDto(id2.toString(), "In Progress", projectId2.toString())

    private val csvRow1 = listOf(id1.toString(), "To Do", projectId1.toString())
    private val csvRow2 = listOf(id2.toString(), "In Progress", projectId2.toString())

    private val csvRows = listOf(csvRow1, csvRow2)

    @BeforeEach
    fun setup() {
        testStateFile = File.createTempFile("test_states", ".csv")
        csvTaskStateDataSource = CsvTaskStateDataSource(testStateFile, taskStateDtoParser, csvHandler)
    }

    @Test
    fun `should return true when state is created successfully`() = runTest {
        every { taskStateDtoParser.toType(any()) } returns csvRow1
        val result = csvTaskStateDataSource.createTaskState(stateDto1)
        assertThat(result).isTrue()
    }

    @Test
    fun `should return true when state is edited successfully`() = runTest {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { taskStateDtoParser.fromType(csvRow1) } returns stateDto1
        every { taskStateDtoParser.fromType(csvRow2) } returns stateDto2
        every { csvHandler.write(any(), any(), any()) } just Runs
        every { taskStateDtoParser.toType(any()) } returns csvRow2

        val result = csvTaskStateDataSource.editTaskState(
            TaskStateDto(id2.toString(), "Review", projectId2.toString())
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to edit non-existing state`() = runTest {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { taskStateDtoParser.fromType(csvRow1) } returns stateDto1
        every { taskStateDtoParser.fromType(csvRow2) } returns stateDto2

        val result = csvTaskStateDataSource.editTaskState(
            TaskStateDto(UUID.randomUUID().toString(), "Blocked", UUID.randomUUID().toString())
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when state is deleted`() = runTest {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { taskStateDtoParser.fromType(csvRow1) } returns stateDto1
        every { taskStateDtoParser.fromType(csvRow2) } returns stateDto2
        every { taskStateDtoParser.toType(any()) } returnsMany listOf(csvRow2)
        every { csvHandler.write(any(), any(), any()) } just Runs

        val result = csvTaskStateDataSource.deleteTaskState(id1.toString())
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when trying to delete non-existing state`() = runTest {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { taskStateDtoParser.fromType(csvRow1) } returns stateDto1
        every { taskStateDtoParser.fromType(csvRow2) } returns stateDto2

        val result = csvTaskStateDataSource.deleteTaskState(UUID.randomUUID().toString())
        assertThat(result).isFalse()
    }

    @Test
    fun `should return all states parsed from csv`() = runTest {
        every { csvHandler.read(testStateFile) } returns csvRows
        every { taskStateDtoParser.fromType(csvRow1) } returns stateDto1
        every { taskStateDtoParser.fromType(csvRow2) } returns stateDto2

        val result = csvTaskStateDataSource.getTaskStates()

        assertThat(result).containsExactly(stateDto1, stateDto2)
    }
}