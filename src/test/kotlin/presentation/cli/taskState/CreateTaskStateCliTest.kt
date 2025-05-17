package presentation.cli.taskState

import domain.usecases.taskState.CreateTaskStateUseCase
import dummyData.createDummyProject
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import presentation.cli.taskState.CreateTaskStateCli.Messages.ERROR
import java.util.*
import javax.naming.InvalidNameException
import kotlin.test.Test

class CreateTaskStateCliTest {
    private val createTaskStateUseCase: CreateTaskStateUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val inputHandler: TaskStateInputHandler = mockk(relaxed = true)
    private lateinit var createTaskStateCli: CreateTaskStateCli

    private val taskState = DummyTaskState.todo
    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        createTaskStateCli =
            CreateTaskStateCli(createTaskStateUseCase, uiController, inputHandler)
    }

    @Test
    fun `should call execute when creating task state succeeds`() = runTest {

        coEvery { createTaskStateUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf(
            taskState.id.toString(),
            taskState.title,
            taskState.projectId.toString()
        )

        createTaskStateCli.createTaskState(dummyProject.id)

        coVerify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when failed to create task state`() = runTest {
        coEvery { createTaskStateUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf(
            taskState.id.toString(),
            taskState.title,
            taskState.projectId.toString()
        )
        1
        createTaskStateCli.createTaskState(dummyProject.id)

        coVerify { createTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `when useCase throws exception should print error message`() = runTest {
        // given
        val projectId = UUID.fromString("00000000-1000-0000-0000-000000000000")
        val dummyState = taskState
        coEvery { inputHandler.readAndValidateUserInputs(projectId = projectId) } returns dummyState

        val exception = InvalidNameException()
        coEvery { createTaskStateUseCase.execute(dummyState.copy(projectId = projectId)) } throws exception

        // when
        createTaskStateCli.createTaskState(projectId)

        // then
        coVerify {
            uiController.printMessage(ERROR.format(exception.localizedMessage))
        }
    }
}
