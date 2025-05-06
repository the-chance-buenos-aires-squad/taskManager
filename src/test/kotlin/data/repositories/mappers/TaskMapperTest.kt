package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import domain.entities.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class TaskMapperTest {

    private lateinit var taskMapper: TaskMapper
    private lateinit var sampleTask: Task
    private lateinit var sampleTaskRow: List<String>
    private val testId = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val testProjectId = UUID.fromString("00000000-0000-0000-0000-000000000002")
    private val testStateId = UUID.fromString("00000000-1000-0000-0000-000000000000")
    private val testAssignedTo = UUID.fromString("00000000-0000-0000-0000-000000000004")
    private val testCreatedBy = UUID.fromString("00000000-0000-0000-0000-000000000005")
    private val testDateTime = LocalDateTime.parse("2025-01-01T10:00:00")

    @BeforeEach
    fun setUp() {
        taskMapper = TaskMapper()
        sampleTask = Task(
            id = testId,
            title = "Test Task",
            description = "Test Description",
            projectId = testProjectId,
            stateId = testStateId,
            assignedTo = testAssignedTo,
            createdBy = testCreatedBy,
            createdAt = testDateTime,
            updatedAt = testDateTime
        )

        sampleTaskRow = listOf(
            testId.toString(),
            "Test Task",
            "Test Description",
            testProjectId.toString(),
            testStateId.toString(),
            testAssignedTo.toString(),
            testCreatedBy.toString(),
            testDateTime.toString(),
            testDateTime.toString()
        )
    }

    @Test
    fun `should convert Task to correct list of strings`() {
        // When
        val result = taskMapper.mapEntityToRow(sampleTask)

        // Then
        assertThat(result).isEqualTo(sampleTaskRow)
    }

    @Test
    fun `should convert list of strings to correct Task`() {
        // When
        val result = taskMapper.mapRowToEntity(sampleTaskRow)

        // Then
        assertThat(result).isEqualTo(sampleTask)
    }

}