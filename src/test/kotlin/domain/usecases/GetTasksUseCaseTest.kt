package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskRepository
import dummyData.DummyTask.dummyTodoTask
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTasksUseCaseTest {

    private lateinit var getTasksUseCase: GetTasksUseCase
    private  var taskRepository: TaskRepository = mockk()

    @BeforeEach
    fun setup() {
        getTasksUseCase = GetTasksUseCase(taskRepository)
    }

    @Test
    fun `should return list of all projects success`() {
        every { taskRepository.getAllTasks() } returns listOf(dummyTodoTask)

        val result = getTasksUseCase.getTasks()

        assertThat(result).isNotNull()
    }

    @Test
    fun `should return false if project don't created`() {
        every { taskRepository.getAllTasks() } returns emptyList()

        val result = getTasksUseCase.getTasks()

        assertThat(result).isNull()
    }
}
