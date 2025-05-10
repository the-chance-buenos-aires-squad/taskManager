package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dto.TaskStateDto
import dummyData.dummyStateData.DummyTaskState
import org.junit.jupiter.api.Test


class TaskStateMapperTest {

    private val entityUser = DummyTaskState.todo
    private val taskStateDtoMapper = TaskStateDtoMapper()

    @Test
    fun `should return dto object when mapping from entity to dto`() {
        //when
        val result = taskStateDtoMapper.fromEntity(entityUser)

        //then
        assertThat(result).isInstanceOf(TaskStateDto::class.java)
    }

}
