package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskRepository
import dummyData.createDummyTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class executeUseCaseTest {

    private lateinit var getTasksUseCase: GetTasksUseCase
    private var taskRepository: TaskRepository = mockk()

    val dummyTodoTask = createDummyTask(
        title = "Task Todo Title",
        description = "task Todo description",
        projectId = UUID.randomUUID(),
    )

    @BeforeEach
    fun setup() {
        getTasksUseCase = GetTasksUseCase(taskRepository)
    }

    @Test
    fun `should return list of tasks success`() = runTest {
        //given
        coEvery { taskRepository.getAllTasks() } returns listOf(dummyTodoTask)

        //when
        val result = getTasksUseCase.execute()

        //then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return empty when no task created`() = runTest {
        //given
        coEvery { taskRepository.getAllTasks() } returns emptyList()

        //when
        val result = getTasksUseCase.execute()

        //then
        assertThat(result).isEmpty()
    }
}
