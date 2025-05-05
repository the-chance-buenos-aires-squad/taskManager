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
        createTaskStateCli = CreateTaskStateCli(createTaskStateUseCase, existsTaskStateUseCase,uiController, inputValidator)
    }

    @Test
    fun `should call execute when creating task state succeeds`() {

        every { createTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(taskState.id,taskState.name,taskState.projectId)

        createTaskStateCli.createTaskState(dummyProject.id)

        verify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when failed to create task state`() {
        every { createTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf(taskState.id,taskState.name,taskState.projectId)

        createTaskStateCli.createTaskState(dummyProject.id)

        verify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when enter empty ID and failed to create task state`() {
        every { uiController.readInput() } returnsMany listOf("", taskState.name, taskState.id)

        val exception = assertThrows<InvalidIdException> {
            createTaskStateCli.createTaskState(dummyProject.id)
        }
        assertThat(exception.message).isEqualTo("ID can't be empty")
    }

    @Test
    fun `should throw exception when name is less than 2 letters and fail to create task state`() {

        every { uiController.readInput() } returnsMany listOf(taskState.id, "A", taskState.projectId)

        val exception = assertThrows<InvalidNameException> {
            createTaskStateCli.createTaskState(dummyProject.id)
        }

        assertThat(exception.message).isEqualTo("Name must be at least 2 letters")
    }

    @Disabled
    @Test
    fun `should throw exception when project id does not start with P and followed by numbers`() {
        every { uiController.readInput() } returnsMany listOf(taskState.id, taskState.name, "X01")

        val exception = assertThrows<InvalidProjectIdException> {
            createTaskStateCli.createTaskState(dummyProject.id)
        }
        assertThat(exception.message).isEqualTo("Project ID must start with 'P' followed by at least two digits (e.g., P01, P123)")
    }
}