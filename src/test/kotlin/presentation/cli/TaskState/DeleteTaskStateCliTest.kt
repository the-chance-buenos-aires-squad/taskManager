package presentation.cli.TaskState

import domain.usecases.taskState.DeleteTaskStateUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test
import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidIdException
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.dummyStateData.DummyTaskState


class DeleteTaskStateCliTest {
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase = mockk(relaxed = true)
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var deleteTaskStateCli: DeleteTaskStateCli

    private val taskStates = listOf(
        DummyTaskState.todo,
        DummyTaskState.inProgress
    )


    @BeforeEach
    fun setup() {
        deleteTaskStateCli = DeleteTaskStateCli(deleteTaskStateUseCase, getAllTaskStatesUseCase,uiController)
    }


    @Test
    fun `should call execute when delete task state successfully`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "1"
        every { deleteTaskStateUseCase.execute(taskStates[0].id) } returns true

        deleteTaskStateCli.deleteTaskState()

        verify { deleteTaskStateUseCase.execute(taskStates[0].id) }
    }

    @Test
    fun `should call execute when failed to delete task state`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "2"
        every { deleteTaskStateUseCase.execute(taskStates[1].id) } returns false

        deleteTaskStateCli.deleteTaskState()

        verify { deleteTaskStateUseCase.execute(taskStates[1].id) }
    }

    @Test
    fun `should print invalid selection when input is not a number`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "ab"

        deleteTaskStateCli.deleteTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should show invalid selection when index is less than 1`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "0"

        deleteTaskStateCli.deleteTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print invalid selection when input is out of range`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "5"

        deleteTaskStateCli.deleteTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print message when no task states available`() {
        every { getAllTaskStatesUseCase.execute() } returns emptyList()

        deleteTaskStateCli.deleteTaskState()

        verify { uiController.printMessage("No task states available to delete.") }
    }

}