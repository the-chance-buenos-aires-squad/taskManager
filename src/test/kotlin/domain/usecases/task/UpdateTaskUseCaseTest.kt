/*
package domain.usecases.task

//todo fix Me
import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import presentation.exceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.audit.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        coEvery { taskRepository.updateTask(any()) } returns true

        //.when
        val result = updateTaskUseCase.execute(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = null,
        )

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when  updating unSuccessful`() = runTest {
        //given
        coEvery { taskRepository.updateTask(any()) } returns false

        //.when
        val result = updateTaskUseCase.execute(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )

        //then
        assertThat(result).isFalse()
    }
}*/
