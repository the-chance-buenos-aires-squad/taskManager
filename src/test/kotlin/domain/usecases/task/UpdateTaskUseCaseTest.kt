package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.Task
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
    fun `should return true when current user is logged in and updating Successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.updateTask(any()) } returns true

        //.when
        val result = updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
            createdBy = DummyTasks.validTask.createdBy
        )

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when current user is logged in and updating unSuccessful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.updateTask(any()) } returns false

        //.when
        val result = updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
            createdBy = DummyTasks.validTask.createdBy
        )

        //then
        assertThat(result).isFalse()
    }


    @Test
    fun `should through UserNotLoggedInException when user not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns null

        //when & then
        assertThrows<UserNotLoggedInException> {
            updateTaskUseCase.updateTask(
                id = DummyTasks.validTask.id,
                title = DummyTasks.validTask.title,
                description = DummyTasks.validTask.description,
                projectId = DummyTasks.validTask.projectId,
                stateId = DummyTasks.validTask.stateId,
                assignedTo = DummyTasks.validTask.assignedTo,
                createdBy = DummyTasks.validTask.createdBy
            )
        }

    }



    @Test
    fun `should add audit when user is logged in and updating is successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        every { taskRepository.updateTask(any()) } returns true

        //when
        updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo,
            createdBy = DummyTasks.validTask.createdBy
        )

        //then
        verify { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should set assignedTo to null when assignedTo is not provided`() {
        //given
        val taskSlot = slot<Task>()
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.updateTask(capture(taskSlot)) } returns true

        //when
        updateTaskUseCase.updateTask(
            id = DummyTasks.validTask.id,
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            createdBy = DummyTasks.validTask.createdBy
        )

        //then
        assertThat(taskSlot.captured.assignedTo).isNull()
    }

}