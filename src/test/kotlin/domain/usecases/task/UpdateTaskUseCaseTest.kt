package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UpdateTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @BeforeEach
    fun setUp() {
        updateTaskUseCase = UpdateTaskUseCase(taskRepository, addAuditUseCase, authRepository)
    }

    @Test
    fun `should return true when current user is logged in and updating Successful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        coEvery { taskRepository.updateTask(any()) } returns true

        //.when
        val result = updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when current user is logged in and updating unSuccessful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        coEvery { taskRepository.updateTask(any()) } returns false

        //.when
        val result = updateTaskUseCase.updateTask(
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

    @Test
    fun `should through UserNotLoggedInException when user not logged in`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns null

        //when & then
        assertThrows<UserNotLoggedInException> {
            updateTaskUseCase.updateTask(
                id = DummyTasks.validTask.id,
                title = DummyTasks.validTask.title,
                description = DummyTasks.validTask.description,
                projectId = DummyTasks.validTask.projectId,
                stateId = DummyTasks.validTask.stateId,
                assignedTo = DummyTasks.validTask.assignedTo,
            )
        }
    }

    @Test
    fun `should add audit when user is logged in and updating is successful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        coEvery { taskRepository.updateTask(any()) } returns true

        //when
        updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
        )

        //then
        coVerify { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() = runTest {
        //given
        val taskSlot = slot<Task>()
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        coEvery { taskRepository.updateTask(capture(taskSlot)) } returns true

        //when
        updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
        )

        //then
        assertThat(taskSlot.captured.assignedTo).isNull()
    }
}