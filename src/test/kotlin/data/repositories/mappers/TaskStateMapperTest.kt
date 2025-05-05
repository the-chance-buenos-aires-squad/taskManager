package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import dummyData.dummyStateData.DummyTaskState
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class TaskStateMapperTest {
    private val taskStateMapper = TaskStateMapper()

    @Test
    fun `should return TaskState object when Calling mapRowToEntity`() {
        val expectedRow = DummyTaskState.readyForReview

        val actualRow = taskStateMapper.mapEntityToRow(expectedRow)

        assertThat(actualRow).hasSize(3)
    }

    @Test
    fun `mapRowToEntity should convert list of strings to TaskState correctly`() {

        val expectedRow = DummyTaskState.blocked
        val row = listOf(expectedRow.id.toString(), "Blocked", "P004")

        val actualRow = taskStateMapper.mapRowToEntity(row)

        assertThat(actualRow.id).isEqualTo(expectedRow.id)
    }
}