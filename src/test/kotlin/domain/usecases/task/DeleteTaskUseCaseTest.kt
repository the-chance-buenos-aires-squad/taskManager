package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserNotLoggedInException
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteTaskUseCaseTest {

    private val taskRepository: TaskRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase


    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, addAuditUseCase, authRepository)
    }


    @Test
    fun `should return true when current user is logged in and deletion Successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.deleteTask(any()) } returns true

        //.when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when current user is logged in and deletion unSuccessful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.deleteTask(any()) } returns false

        //.when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isFalse()
    }


    @Test
    fun `should through UserNotLoggedInException when user not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns null

        //when & then
        assertThrows<UserNotLoggedInException> {
            deleteTaskUseCase.deleteTask(UUID.randomUUID())
        }


    }


    @Test
    fun `should add audit when user is logged in and deletion is successful`() {
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        every { taskRepository.deleteTask(any()) } returns true

        //when
        deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        verify { addAuditUseCase.addAudit(any(), any(), any(), any(), any(), any(), any()) }
    }

}