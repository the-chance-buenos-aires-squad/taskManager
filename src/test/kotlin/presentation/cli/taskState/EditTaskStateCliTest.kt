package presentation.cli.taskState

import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.createDummyProject
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test

class EditTaskStateCliTest {
    private val editTaskStateUseCase: EditTaskStateUseCase = mockk(relaxed = true)
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var editTaskStateCli: EditTaskStateCli

    private val taskState = DummyTaskState.todo
    private val taskStates = listOf(DummyTaskState.todo, DummyTaskState.inProgress)

    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        editTaskStateCli = EditTaskStateCli(editTaskStateUseCase, getAllTaskStatesUseCase, uiController)
    }

    @Test
    fun `should call execute when editing task state succeeds`() = runTest{
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns listOf(taskState)
        every { uiController.readInput() } returnsMany listOf("1", taskState.name)
        coEvery { editTaskStateUseCase.execute(any()) } returns true

        editTaskStateCli.editTaskState(dummyProject.id)

        coVerify { editTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when editing task state fails`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns listOf(taskState)
        every { uiController.readInput() } returnsMany listOf("1", taskState.name)
        coEvery { editTaskStateUseCase.execute(any()) } returns false

        editTaskStateCli.editTaskState(dummyProject.id)

        coVerify { editTaskStateUseCase.execute(any()) }
    }


    @Test
    fun `should print message when no task states available`() = runTest{
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns emptyList()

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { uiController.printMessage("No task states available to edit.") }
    }

    @Test
    fun `should print invalid selection message when input is not a valid index`()  = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "99"

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print invalid selection when input is not a number`()  = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "ab"

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should show invalid selection when index is less than 1`()  = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "0"

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { uiController.printMessage("Invalid selection.") }
    }


}