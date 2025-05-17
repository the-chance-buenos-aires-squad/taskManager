import domain.entities.TaskState
import domain.usecases.task.AddTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.TaskStateCliHelper
import presentation.cli.task.CreateTaskCli
import java.util.*

class CreateTaskCliTest {

    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var taskCliHelper: TaskStateCliHelper
    private lateinit var uiController: UiController
    private lateinit var createTaskCli: CreateTaskCli

    @BeforeEach
    fun setUp() {
        addTaskUseCase = mockk(relaxed = true)
        taskCliHelper = mockk()
        uiController = mockk(relaxed = true)

        createTaskCli = CreateTaskCli(addTaskUseCase, taskCliHelper, uiController)
    }

    @Test
    fun `addTask should create task successfully`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val taskStateId = UUID.randomUUID()
        val taskStates = listOf(
            TaskState(id = taskStateId, title = "To Do", projectId = projectId)
        )

        every { uiController.readInput() } returnsMany listOf("My Title", "Some description", "mostafa")
        coEvery { taskCliHelper.getTaskStates(projectId) } returns taskStates
        every { taskCliHelper.selectTaskState(taskStates) } returns taskStates[0]

        // When
        createTaskCli.addTask(projectId)

        // Then
        coVerify {
            addTaskUseCase.execute(
                title = "My Title",
                description = "Some description",
                projectId = projectId,
                stateId = taskStateId,
                assignedTo = "mostafa"
            )
        }

        verify {
            uiController.printMessage("add Task Successful")
        }
    }
}