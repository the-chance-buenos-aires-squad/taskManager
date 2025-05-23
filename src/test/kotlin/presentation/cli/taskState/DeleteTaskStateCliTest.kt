package presentation.cli.taskState

import domain.usecases.taskState.DeleteTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.dummyStateData.DummyTaskState
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import java.util.*
import kotlin.test.Test


class DeleteTaskStateCliTest {
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase = mockk(relaxed = true)
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk()
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var deleteTaskStateCli: DeleteTaskStateCli

    private val taskStates = listOf(
        DummyTaskState.todo,
        DummyTaskState.inProgress
    )


    @BeforeEach
    fun setup() {
        deleteTaskStateCli = DeleteTaskStateCli(deleteTaskStateUseCase, getAllTaskStatesUseCase, uiController)
    }


    @Test
    fun `should call execute when delete task state successfully`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "1"
        coEvery { deleteTaskStateUseCase.execute(taskStates[0].id) } returns true

        deleteTaskStateCli.deleteTaskState(taskStates[0].projectId)

        coVerify { deleteTaskStateUseCase.execute(taskStates[0].id) }
    }

    @Test
    fun `should call execute when failed to delete task state`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "2"
        coEvery { deleteTaskStateUseCase.execute(taskStates[1].id) } returns false

        deleteTaskStateCli.deleteTaskState(taskStates[1].projectId)

        coVerify { deleteTaskStateUseCase.execute(taskStates[1].id) }
    }

    @Test
    fun `should print invalid selection when input is not a number`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "ab"

        deleteTaskStateCli.deleteTaskState(UUID.randomUUID())

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should show invalid selection when index is less than 1`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "0"

        deleteTaskStateCli.deleteTaskState(UUID.randomUUID())

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print invalid selection when input is out of range`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns taskStates
        every { uiController.readInput() } returns "5"

        deleteTaskStateCli.deleteTaskState(UUID.randomUUID())

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print message when no task states available`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns emptyList()

        deleteTaskStateCli.deleteTaskState(UUID.randomUUID())

        verify { uiController.printMessage("No task states available to delete.") }
    }
}