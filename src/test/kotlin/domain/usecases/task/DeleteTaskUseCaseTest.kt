package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import presentation.exceptions.UserNotLoggedInException
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.audit.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
class DeleteTaskUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `should return true deletion Successful`() = runTest {
        //given
        coEvery { taskRepository.deleteTask(any()) } returns true

        //.when
        val result = deleteTaskUseCase.execute(UUID.randomUUID())

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when  deletion unSuccessful`() = runTest {
        //given
        coEvery { taskRepository.deleteTask(any()) } returns false

        //.when
        val result = deleteTaskUseCase.execute(UUID.randomUUID())

        //then
        assertThat(result).isFalse()
    }
}