package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import domain.usecases.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class DeleteTaskUseCaseTest{
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)
    private lateinit var deleteTaskUseCase:DeleteTaskUseCase



    @BeforeEach
    fun setUp(){
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, addAuditUseCase, authRepository)
    }


    @Test
    fun `should return true when deleting task successfully`(){
        //given
        every { taskRepository.deleteTask(any()) } returns true
        //when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when deleting task unSuccessfully`(){
        //given
        every { taskRepository.deleteTask(any()) } returns false
        //when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isFalse()
    }


    @Test
    fun `should return false when current user is logged out`(){
        //given
        every { authRepository.getCurrentUser() } returns null

        //.when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isFalse()

    }

    @Test
    fun `should return true when current user is logged in and deletion successful`(){
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.deleteTask(any()) } returns true

        //.when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when current user is logged in and deletion unSuccessful`(){
        //given
        every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
        every { taskRepository.deleteTask(any()) } returns false

        //.when
        val result = deleteTaskUseCase.deleteTask(UUID.randomUUID())

        //then
        assertThat(result).isFalse()
    }

}