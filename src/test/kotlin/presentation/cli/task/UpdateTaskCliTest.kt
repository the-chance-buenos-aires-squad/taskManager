package presentation.cli.task

import data.dataSource.dummyData.DummyTasks
import presentation.exceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.repositories.UserRepository
import domain.usecases.task.GetAllTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.util.*

class UpdateTaskCliTest {

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var getAllTaskStatesUseCase: GetAllTaskStatesUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var uiController: UiController
    private lateinit var updateTaskCli: UpdateTaskCli


    private val projectId = UUID.randomUUID()
    private val createdBy = UUID.randomUUID()
    private val task = DummyTasks.validTask.copy(projectId = projectId, createdBy = createdBy)
    private val taskState = TaskState(UUID.randomUUID(), "In Progress", projectId)

    @BeforeEach
    fun setup() {
        getAllTasksUseCase = mockk()
        updateTaskUseCase = mockk()
        getAllTaskStatesUseCase = mockk()
        userRepository = mockk()
        uiController = mockk(relaxed = true)

        updateTaskCli = UpdateTaskCli(
            getAllTasksUseCase,
            updateTaskUseCase,
            getAllTaskStatesUseCase,
            userRepository,
            uiController
        )

        mockkObject(TaskCliUtils)
    }

    @Test
    fun `should update task successfully`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { getAllTaskStatesUseCase.execute(projectId) } returns listOf(taskState)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        coEvery {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } returns true

        updateTaskCli.update(projectId)

        coVerify { uiController.printMessage("Task updated successfully.") }
    }

    @Test
    fun `should print failed to update task`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { getAllTaskStatesUseCase.execute(projectId) } returns listOf(taskState)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        coEvery {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } returns false

        updateTaskCli.update(projectId)

        coVerify { uiController.printMessage("Failed to update task.") }
    }

    @Test
    fun `should handle UserNotLoggedInException when updating task`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { getAllTaskStatesUseCase.execute(projectId) } returns listOf(taskState)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        coEvery {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } throws UserNotLoggedInException("User not logged in")

        updateTaskCli.update(projectId)

        coVerify { uiController.printMessage("User not logged in") }
    }

    @Test
    fun `should return when no tasks are found`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf()
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf()

        updateTaskCli.update(projectId)

        coVerify(exactly = 0) { TaskCliUtils.selectTask(any(), any()) }
        coVerify(exactly = 0) { updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should return when no task is selected`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns null

        updateTaskCli.update(projectId)

        coVerify(exactly = 0) { updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any()) }
    }
}