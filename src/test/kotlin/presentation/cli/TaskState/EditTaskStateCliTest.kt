package presentation.cli.TaskState

import TaskStateInputValidator
import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidIdException
import domain.customeExceptions.InvalidTaskStateNameException
import domain.customeExceptions.InvalidProjectIdException
import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import java.util.UUID
import kotlin.test.Test

class EditTaskStateCliTest {
    private val editTaskStateUseCase: EditTaskStateUseCase = mockk(relaxed = true)
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val inputValidator = TaskStateInputValidator()
    private lateinit var editTaskStateCli: EditTaskStateCli

    private val taskState = DummyTaskState.todo
    private  val taskStates = listOf(DummyTaskState.todo, DummyTaskState.inProgress)

    @BeforeEach
    fun setup() {
        editTaskStateCli = EditTaskStateCli(editTaskStateUseCase, getAllTaskStatesUseCase ,uiController, inputValidator)
    }

    @Test
    fun `should call execute when editing task state succeeds`() {
        every { getAllTaskStatesUseCase.execute() } returns listOf(taskState)
        every { editTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf("1" , taskState.name,taskState.projectId)

        editTaskStateCli.editTaskState()

        verify { editTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when editing task state fails`() {
        every { getAllTaskStatesUseCase.execute() } returns listOf(taskState)
        every { editTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf("1" , taskState.name, taskState.projectId)

        editTaskStateCli.editTaskState()

        verify { editTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should print message when no task states available`() {
        every { getAllTaskStatesUseCase.execute() } returns emptyList()

        editTaskStateCli.editTaskState()

        verify { uiController.printMessage("No task states available to edit.") }
    }

    @Test
    fun `should print invalid selection message when input is not a valid index`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "99"

        editTaskStateCli.editTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should print invalid selection when input is not a number`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "ab"

        editTaskStateCli.editTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

    @Test
    fun `should show invalid selection when index is less than 1`() {
        every { getAllTaskStatesUseCase.execute() } returns taskStates
        every { uiController.readInput() } returns "0"

        editTaskStateCli.editTaskState()

        verify { uiController.printMessage("Invalid selection.") }
    }

}