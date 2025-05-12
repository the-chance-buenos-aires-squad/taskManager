package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserNotLoggedInException
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class executeUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, addAuditUseCase, authRepository)
    }

    @Test
    fun `should return true when current user is logged in and deletion Successful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        coEvery { taskRepository.deleteTask(any()) } returns true

        //.when
        val result = deleteTaskUseCase.execute(UUID.randomUUID())

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when current user is logged in and deletion unSuccessful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        coEvery { taskRepository.deleteTask(any()) } returns false

        //.when
        val result = deleteTaskUseCase.execute(UUID.randomUUID())

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should through UserNotLoggedInException when user not logged in`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns null

        //when & then
        assertThrows<UserNotLoggedInException> {
            deleteTaskUseCase.execute(UUID.randomUUID())
        }
    }

    @Test
    fun `should add audit when user is logged in and deletion is successful`() = runTest {
        //given
        coEvery { authRepository.getCurrentUser() } returns DummyUser.dummyUserOne
        coEvery { taskRepository.deleteTask(any()) } returns true

        //when
        deleteTaskUseCase.execute(UUID.randomUUID())

        //then
        coVerify { addAuditUseCase.execute(any(), any(), any(), any(), any(), any(), any()) }
    }
}