package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidProjectIdException
import domain.customeExceptions.TaskDescriptionEmptyException
import domain.customeExceptions.TaskTitleEmptyException
import domain.entities.Task
import domain.repositories.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateTaskUseCaseTest {

    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private lateinit var addTaskUseCase: AddTaskUseCase
    private val validTitle = "Valid Task Title"
    private val validDescription = "Valid Task Description"
    private val validProjectId = UUID.randomUUID()
    private val validStateId = UUID.randomUUID()
    private val validAssignedToId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        addTaskUseCase = AddTaskUseCase(taskRepository)
    }

    @Test
    fun `should create task successfully when all parameters are valid`() = runTest {
        coEvery {
            taskRepository.addTask(
                any(), any(), any(), any(), any()
            )
        } returns true

        val result = addTaskUseCase.execute(
            title = validTitle,
            description = validDescription,
            projectId = validProjectId,
            stateId = validStateId,
            assignedTo = validAssignedToId.toString(),
        )

        assertThat(result).isTrue()
        coVerify(exactly = 1) {
            taskRepository.addTask(
                any(), any(), any(), any(), any(),
            )
        }
    }

    @Test
    fun `should return false when create task unSuccessfully`() = runTest {
        coEvery {
            taskRepository.addTask(
                any(), any(), any(), any(), any()
            )
        } returns false
        val result = addTaskUseCase.execute(
            validTitle, validDescription, validProjectId, validStateId, validAssignedToId.toString()
        )

        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskRepository.addTask(any(), any(), any(), any(), any()) }
    }



    @Test
    fun `should return true when task is successfully created`() = runTest {
        coEvery { taskRepository.addTask(
            any(), any(), any(), any(), any()
        ) } returns true

        val result = addTaskUseCase.execute(
            validTitle,
            validDescription,
            validProjectId,
            validStateId,
            validAssignedToId.toString(),
        )

        assertThat(result).isTrue()
    }


}
