package presentation.cli.taskState

import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import dummyData.createDummyProject
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
    private lateinit var createTaskStateCli: CreateTaskStateCli

    private val taskState = DummyTaskState.todo
    val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        createTaskStateCli =
            CreateTaskStateCli(createTaskStateUseCase, existsTaskStateUseCase, uiController)
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
    fun `should show message when task state already exists`() {
        val taskStateName = "Existing Task State"
        val projectId = dummyProject.id

        every { uiController.readInput() } returns taskStateName
        every { existsTaskStateUseCase.execute(taskStateName, projectId) } returns true

        // Act
        createTaskStateCli.createTaskState(projectId)

        // Assert
        verify { uiController.printMessage("Task state already exists.") }
    }
}
