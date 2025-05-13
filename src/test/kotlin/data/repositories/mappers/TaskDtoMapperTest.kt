package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import data.dataSource.dummyData.DummyTasks.validTask
import data.dataSource.dummyData.DummyTasks.validTaskDto
import data.dto.TaskDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TaskDtoMapperTest{
    private val taskDtoMapper = TaskDtoMapper()



    @Test
    fun `should return task object when mapping toEntity`(){
        val expectedTask = taskDtoMapper.toEntity(validTaskDto)
        assertThat(expectedTask).isEqualTo(validTask)
    }

    @Test
    fun `should return taskDto object when mapping fromEntity`(){
        val expectedTask = taskDtoMapper.fromEntity(validTask)
        assertThat(expectedTask).isEqualTo(validTaskDto)
    }
}