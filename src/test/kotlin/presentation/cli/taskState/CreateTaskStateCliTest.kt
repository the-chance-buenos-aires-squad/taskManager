package presentation.cli.taskState

import TaskStateInputValidator
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test

class CreateTaskStateCliTest {
    private val createTaskStateUseCase: CreateTaskStateUseCase = mockk(relaxed = true)
    private val existsTaskStateUseCase: ExistsTaskStateUseCase = mockk(relaxed = true)

    private val uiController: UiController = mockk(relaxed = true)
    private val inputValidator = TaskStateInputValidator()
    private lateinit var createTaskStateCli: CreateTaskStateCli

    private val taskState = DummyTaskState.todo

    @BeforeEach
    fun setup() {
        createTaskStateCli = CreateTaskStateCli(createTaskStateUseCase, existsTaskStateUseCase,uiController, inputValidator)
    }

    @Test
    fun `should call execute when creating task state succeeds`() {

        every { createTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(taskState.name,taskState.projectId)

        createTaskStateCli.createTaskState()

        verify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when failed to create task state`() {
        every { createTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf(taskState.name,taskState.projectId)

        createTaskStateCli.createTaskState()

        verify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should show message when task state already exists`() {
        every { uiController.readInput() } returnsMany listOf(taskState.name, taskState.projectId)
        every { existsTaskStateUseCase.execute(taskState.name, taskState.projectId) } returns true

        createTaskStateCli.createTaskState()

        verify { uiController.printMessage("Task state already exists.") }
    }

}