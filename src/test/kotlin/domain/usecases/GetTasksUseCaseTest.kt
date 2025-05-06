package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskRepository
import dummyData.createDummyTask
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class GetTasksUseCaseTest {

    private lateinit var getTasksUseCase: GetTasksUseCase
    private  var taskRepository: TaskRepository = mockk()

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
    fun `should return list of tasks success`() {
        //given
        every { taskRepository.getAllTasks() } returns listOf(dummyTodoTask)

        //when
        val result = getTasksUseCase.getTasks()

        //then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return empty when no task created`() {
        //given
        every { taskRepository.getAllTasks() } returns emptyList()

        //when
        val result = getTasksUseCase.getTasks()

        //then
        assertThat(result).isEmpty()
    }
}
