package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import dummyData.dummyStateData.DummyTaskState
import kotlin.test.Test

class TaskStateMapperTest {
    private val taskStateDtoMapper = TaskStateDtoMapper()

    @Test
    fun `should return TaskState object when Calling mapRowToEntity`() {
        val expectedRow = DummyTaskState.readyForReview

        val actualRow = taskStateDtoMapper.fromEntity(expectedRow)

        assertThat(actualRow).hasSize(3)
    }

    @Test
    fun `mapRowToEntity should convert list of strings to TaskState correctly`() {

        val expectedRow = DummyTaskState.blocked
        val row = listOf("00000000-4000-0000-0000-000000000000", "Blocked", "40000000-1000-0000-0000-000000000000")

        val actualRow = taskStateDtoMapper.mapRowToEntity(row)

        assertThat(actualRow.id).isEqualTo(expectedRow.id)
    }
}