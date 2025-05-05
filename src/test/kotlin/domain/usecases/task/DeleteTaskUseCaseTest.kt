package domain.usecases.task

import domain.repositories.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteTaskUseCaseTest {

    private val taskRepository: TaskRepository = mockk()
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `should return true when task is deleted successfully`() {
        val taskId = UUID.randomUUID()
        every { taskRepository.deleteTask(taskId) } returns true

        val result = deleteTaskUseCase.execute(taskId)

        assertTrue(result)
        verify { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `should return false when task is not deleted`() {
        val taskId = UUID.randomUUID()
        every { taskRepository.deleteTask(taskId) } returns false

        val result = deleteTaskUseCase.execute(taskId)

        assertFalse(result)
        verify { taskRepository.deleteTask(taskId) }
    }
}