package presentation.cli.task

import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.UserNotLoggedInException
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetAllTasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import presentation.UiController
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteTaskCliTest {

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var uiController: UiController
    private lateinit var deleteTaskCli: DeleteTaskCli

    private val projectId = UUID.randomUUID()
    private val task1 = DummyTasks.validTask.copy(title = "Task One", projectId = projectId)
    private val task2 = DummyTasks.validTask.copy(title = "Task Two", projectId = projectId)

    @BeforeTest
    fun setup() {
        getAllTasksUseCase = mockk()
        deleteTaskUseCase = mockk()
        uiController = mockk(relaxed = true)
        deleteTaskCli = DeleteTaskCli(getAllTasksUseCase, deleteTaskUseCase, uiController)
    }

    @Test
    fun `should delete task when valid input and confirmation given`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1, task2)
        coEvery { uiController.readInput() } returns "2" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task2.id) } returns true

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("Task deleted successfully.") }
        coVerify { deleteTaskUseCase.deleteTask(task2.id) }
    }

    @Test
    fun `should cancel deletion if user says no`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "1" andThen "no"

        deleteTaskCli.delete(projectId)

        coVerify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
        coVerify { uiController.printMessage("Deletion canceled. Returning to dashboard.") }
    }

    @Test
    fun `should handle invalid index and retry once`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "999" andThen "1" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        coVerify { deleteTaskUseCase.deleteTask(task1.id) }
        coVerify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should return early if no tasks found`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns emptyList()

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("No tasks found for this project.") }
        coVerify(exactly = 0) { uiController.readInput() }
        coVerify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should handle confirmation with invalid then valid response`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "1" andThen "maybe" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        coVerify { deleteTaskUseCase.deleteTask(task1.id) }
        coVerify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should handle UserNotLoggedInException`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "1" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task1.id) } throws UserNotLoggedInException("Operation not allowed: User is not logged in.")

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("Operation not allowed: User is not logged in.") }
    }

    @Test
    fun `should exit if invalid input provided twice`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "abc" andThen "xyz"

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("Invalid input. Returning to dashboard.") }
        coVerify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should show filtered tasks only for given project`() = runTest {
        val otherProjectId = UUID.randomUUID()
        val taskInOtherProject = DummyTasks.validTask.copy(title = "Other", projectId = otherProjectId)

        coEvery { getAllTasksUseCase.execute() } returns listOf(task1, taskInOtherProject)
        coEvery { uiController.readInput() } returns "1" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        coVerify { deleteTaskUseCase.deleteTask(task1.id) }
        coVerify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should cancel deletion after two invalid confirmation inputs`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "1" andThen "maybe" andThen "what"

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("Invalid confirmation. Returning to dashboard.") }
        coVerify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should show error message when task deletion fails`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task1)
        coEvery { uiController.readInput() } returns "1" andThen "yes"
        coEvery { deleteTaskUseCase.deleteTask(task1.id) } returns false

        deleteTaskCli.delete(projectId)

        coVerify { uiController.printMessage("Error: Task was not deleted.") }
    }
}