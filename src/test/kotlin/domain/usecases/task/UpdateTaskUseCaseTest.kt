package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.repositories.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @BeforeEach
    fun setUp() {
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
    }

    @Test
    fun `should return true when  updating Successful`() = runTest {
        //given
        coEvery { taskRepository.updateTask(
            any(),any(),any(),any(),any(),any(),any()
        ) } returns true

        //.when
        val result = updateTaskUseCase.execute(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = "",
            createdAt = DummyTasks.validTask.createdAt
        )

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when  updating unSuccessful`() = runTest {
        //given
        coEvery { taskRepository.updateTask(
            any(),any(),any(),any(),any(),any(),any()
        ) } returns false

        //.when
        val result = updateTaskUseCase.execute(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = "",
            createdAt = DummyTasks.validTask.createdAt
        )

        //then
        assertThat(result).isFalse()
    }
}
