package presentation.cli.TaskState

import TaskStateInputValidator
import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidIdException
import domain.customeExceptions.InvalidNameException
import domain.customeExceptions.InvalidProjectIdException
import domain.usecases.taskState.EditTaskStateUseCase
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

class EditTaskStateCliTest {
    private val editTaskStateUseCase: EditTaskStateUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val inputValidator = TaskStateInputValidator()
    private lateinit var editTaskStateCli: EditTaskStateCli

    private val taskState = DummyTaskState.todo
    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        editTaskStateCli = EditTaskStateCli(editTaskStateUseCase, uiController, inputValidator)
    }

    @Test
    fun `should call execute when editing task state succeeds`() {
        every { editTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(taskState.id.toString(),taskState.name,taskState.projectId.toString())

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { editTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when editing task state fails`() {
        every { editTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf(taskState.id.toString(), taskState.name, taskState.projectId.toString())

        editTaskStateCli.editTaskState(dummyProject.id)

        verify { editTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when ID is empty`() {
        every { uiController.readInput() } returnsMany listOf("", taskState.name, taskState.projectId.toString())

        val exception = assertThrows<InvalidIdException> {
            editTaskStateCli.editTaskState(dummyProject.id)
        }
        assertThat(exception.message).isEqualTo("New ID can't be empty")
    }

    @Test
    fun `should throw exception when name is less than 2 letters`() {
        every { uiController.readInput() } returnsMany listOf(taskState.id.toString(), "A", taskState.projectId.toString())

        val exception = assertThrows<InvalidNameException> {
            editTaskStateCli.editTaskState(dummyProject.id)
        }
        assertThat(exception.message).isEqualTo("New Name must be at least 2 letters")
    }

    @Disabled
    @Test
    fun `should throw exception when project ID is invalid`() {
        every { uiController.readInput() } returnsMany listOf(taskState.id.toString(), taskState.name, "X01")

        val exception = assertThrows<InvalidProjectIdException> {
            editTaskStateCli.editTaskState(dummyProject.id)
        }
        assertThat(exception.message).isEqualTo("New Project ID must start with 'P' followed by at least two digits (e.g., P01, P123)")
    }


}