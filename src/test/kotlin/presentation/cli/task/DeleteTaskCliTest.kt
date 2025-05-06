package presentation.cli.task

import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.UserNotLoggedInException
import domain.repositories.AuthRepository
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetAllTasksUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `should delete task when valid input and confirmation given`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1, task2)
        every { uiController.readInput() } returns "2" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task2.id) } returns true

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("Task deleted successfully.") }
        verify { deleteTaskUseCase.deleteTask(task2.id) }
    }

    @Test
    fun `should cancel deletion if user says no`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "1" andThen "no"

        deleteTaskCli.delete(projectId)

        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
        verify { uiController.printMessage("Deletion canceled. Returning to dashboard.") }
    }

    @Test
    fun `should handle invalid index and retry once`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "999" andThen "1" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        verify { deleteTaskUseCase.deleteTask(task1.id) }
        verify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should return early if no tasks found`() {
        every { getAllTasksUseCase.execute() } returns emptyList()

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("No tasks found for this project.") }
        verify(exactly = 0) { uiController.readInput() }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should handle confirmation with invalid then valid response`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "1" andThen "maybe" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        verify { deleteTaskUseCase.deleteTask(task1.id) }
        verify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should handle UserNotLoggedInException`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "1" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task1.id) } throws UserNotLoggedInException("Operation not allowed: User is not logged in.")

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("Operation not allowed: User is not logged in.") }
    }

    @Test
    fun `should exit if invalid input provided twice`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "abc" andThen "xyz"

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("Invalid input. Returning to dashboard.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should show filtered tasks only for given project`() {
        val otherProjectId = UUID.randomUUID()
        val taskInOtherProject = DummyTasks.validTask.copy(title = "Other", projectId = otherProjectId)

        every { getAllTasksUseCase.execute() } returns listOf(task1, taskInOtherProject)
        every { uiController.readInput() } returns "1" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task1.id) } returns true

        deleteTaskCli.delete(projectId)

        verify { deleteTaskUseCase.deleteTask(task1.id) }
        verify { uiController.printMessage("Task deleted successfully.") }
    }

    @Test
    fun `should cancel deletion after two invalid confirmation inputs`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "1" andThen "maybe" andThen "what"

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("Invalid confirmation. Returning to dashboard.") }
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
    }

    @Test
    fun `should show error message when task deletion fails`() {
        every { getAllTasksUseCase.execute() } returns listOf(task1)
        every { uiController.readInput() } returns "1" andThen "yes"
        every { deleteTaskUseCase.deleteTask(task1.id) } returns false

        deleteTaskCli.delete(projectId)

        verify { uiController.printMessage("Error: Task was not deleted.") }
    }
}