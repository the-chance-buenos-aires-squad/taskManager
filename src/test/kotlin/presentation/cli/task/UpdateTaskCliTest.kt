package presentation.cli.task

import data.dataSource.dummyData.DummyTasks
import domain.customeExceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.repositories.UserRepository
import domain.usecases.task.GetAllTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
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
    fun `should update task successfully`() {
        every { getAllTasksUseCase.execute() } returns listOf(task)
        every { getAllTaskStatesUseCase.execute(any()) } returns listOf(taskState)
        every { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        every { TaskCliUtils.selectTask(any(), any()) } returns task
        every {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        every {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } returns true

        updateTaskCli.update(projectId)

        verify { uiController.printMessage("Task updated successfully.") }
    }

    @Test
    fun `should print failed to update task`() {
        every { getAllTasksUseCase.execute() } returns listOf(task)
        every { getAllTaskStatesUseCase.execute(any()) } returns listOf(taskState)
        every { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        every { TaskCliUtils.selectTask(any(), any()) } returns task
        every {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        every {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } returns false

        updateTaskCli.update(projectId)

        verify { uiController.printMessage("Failed to update task.") }
    }

    @Test
    fun `should handle UserNotLoggedInException when updating task`() {
        every { getAllTasksUseCase.execute() } returns listOf(task)
        every { getAllTaskStatesUseCase.execute(any()) } returns listOf(taskState)
        every { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        every { TaskCliUtils.selectTask(any(), any()) } returns task
        every {
            TaskCliUtils.promptForUpdatedTask(any(), any(), userRepository, uiController)
        } returns task
        every {
            updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any())
        } throws UserNotLoggedInException("User not logged in")

        updateTaskCli.update(projectId)

        verify { uiController.printMessage("User not logged in") }
    }

    @Test
    fun `should return when no tasks are found`() {
        every { getAllTasksUseCase.execute() } returns listOf()
        every { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf()

        updateTaskCli.update(projectId)

        verify(exactly = 0) { TaskCliUtils.selectTask(any(), any()) }
        verify(exactly = 0) { updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should return when no task is selected`() {
        every { getAllTasksUseCase.execute() } returns listOf(task)
        every { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        every { TaskCliUtils.selectTask(any(), any()) } returns null

        updateTaskCli.update(projectId)

        verify(exactly = 0) { updateTaskUseCase.updateTask(any(), any(), any(), any(), any(), any()) }
    }
}