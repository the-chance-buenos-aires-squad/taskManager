package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import data.dataSource.dummyData.DummyTasks.validTask
import domain.repositories.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetAllTasksUseCaseTest {
    private val taskRepository:TaskRepository = mockk()
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @BeforeTest
    fun setUp(){
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository)
    }

    @Test
    fun `should return list of tasks`()= runTest{
        //given
        val expectedTasks = listOf(validTask, validTask)
        coEvery { taskRepository.getAllTasks() } returns expectedTasks
        //when
        val result = getAllTasksUseCase.execute()

        //then
        assertThat(result).isEqualTo(expectedTasks)

    }

    @Test
    fun `should return empty lis of tasks when no tasks in repository`()= runTest{
        //given
        coEvery { taskRepository.getAllTasks() } returns emptyList()
        //when
        val result = getAllTasksUseCase.execute()

        //then
        assertThat(result).isEmpty()

    }
}