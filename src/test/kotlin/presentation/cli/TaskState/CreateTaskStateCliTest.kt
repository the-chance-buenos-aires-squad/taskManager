package presentation.cli.TaskState

import TaskStateInputValidator
import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidIdException
import domain.customeExceptions.InvalidNameException
import domain.customeExceptions.InvalidProjectIdException
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import dummyData.createDummyProject
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import kotlin.test.Test

class CreateTaskStateCliTest {
    private val createTaskStateUseCase: CreateTaskStateUseCase = mockk(relaxed = true)
    private val existsTaskStateUseCase: ExistsTaskStateUseCase = mockk(relaxed = true)

    private val uiController: UiController = mockk(relaxed = true)
    private val inputValidator = TaskStateInputValidator()
    private lateinit var createTaskStateCli: CreateTaskStateCli

    private val taskState = DummyTaskState.todo
    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        createTaskStateCli =
            CreateTaskStateCli(createTaskStateUseCase, existsTaskStateUseCase, uiController, inputValidator)
    }

    @Test
    fun `should call execute when creating task state succeeds`() {

        every { createTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(
            taskState.id.toString(),
            taskState.name,
            taskState.projectId.toString()
        )

        createTaskStateCli.createTaskState(dummyProject.id)

        verify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when failed to create task state`() {
        every { createTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf(
            taskState.id.toString(),
            taskState.name,
            taskState.projectId.toString()
        )

        createTaskStateCli.createTaskState(dummyProject.id)

        verify { createTaskStateUseCase.execute(any()) }
    }


    @Test
    fun `should throw exception when name is less than 2 letters and fail to create task state`() {
        every { existsTaskStateUseCase.execute(any()) } returns false

        assertThrows<InvalidNameException> {
            createTaskStateCli.createTaskState(dummyProject.id)
        }
    }

    @Test
    fun `should print message when task state already exists`() {
        every { existsTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(
            taskState.id.toString(),
            taskState.name,
            taskState.projectId.toString()
        )

        createTaskStateCli.createTaskState(dummyProject.id)

        verify { uiController.printMessage("Task state already exists.") }
        verify(exactly = 0) { createTaskStateUseCase.execute(any()) }
    }
}
