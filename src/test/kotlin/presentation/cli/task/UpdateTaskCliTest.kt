package presentation.cli.task

import data.dataSource.dummyData.DummyTasks
import presentation.exceptions.UserNotLoggedInException
import domain.entities.TaskState
import domain.repositories.UserRepository
import domain.usecases.task.GetAllTasksUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.TaskStateCliHelper
import java.util.*

class UpdateTaskCliTest {

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var taskCliHelper: TaskStateCliHelper
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
        taskCliHelper = mockk()
        uiController = mockk(relaxed = true)

        updateTaskCli = UpdateTaskCli(
            getAllTasksUseCase,
            updateTaskUseCase,
            taskCliHelper,
            uiController
        )

        mockkObject(TaskCliUtils)
    }

    @Test
    fun `should update task successfully`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery { taskCliHelper.getTaskStates(projectId) } returns listOf(taskState)
        coEvery { taskCliHelper.selectTaskState(any()) } returns taskState
        coEvery { uiController.readInput() } returns "" andThen "" andThen ""

        coEvery {
            updateTaskUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns true

        updateTaskCli.update(projectId)

        coVerify { uiController.printMessage("Task updated successfully.") }
    }

    @Test
    fun `should print failed to update task`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery { taskCliHelper.getTaskStates(projectId) } returns listOf(taskState)
        coEvery { taskCliHelper.selectTaskState(any()) } returns taskState
        coEvery { uiController.readInput() } returns "" andThen "" andThen "" // Empty input triggers fallback to existing task values

        // simulate failure with an exception
        coEvery {
            updateTaskUseCase.execute(
                id = task.id,
                title = task.title,
                description = task.description,
                projectId = projectId,
                stateId = taskState.id,
                assignedTo = task.assignedTo.toString(),
                createdAt = task.createdAt
            )
        } throws RuntimeException("Something went wrong")

        // when
        updateTaskCli.update(projectId)

        // then
        verify {
            uiController.printMessage("Failed to update task. SOMETHING WENT WRONG")
        }
    }

    @Test
    fun `should handle UserNotLoggedInException when updating task`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns task
        coEvery { taskCliHelper.getTaskStates(projectId) } returns listOf(taskState)
        coEvery { taskCliHelper.selectTaskState(any()) } returns taskState
        coEvery { uiController.readInput() } returns "" andThen "" andThen ""

        coEvery {
            updateTaskUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws UserNotLoggedInException("User not logged in")

        updateTaskCli.update(projectId)

        verify() { uiController.printMessage("Failed to update task. USER NOT LOGGED IN") }
    }

    @Test
    fun `should return when no tasks are found`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf()
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf()

        updateTaskCli.update(projectId)

        coVerify(exactly = 0) { TaskCliUtils.selectTask(any(), any()) }
        coVerify(exactly = 0) { updateTaskUseCase.execute(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should return when no task is selected`() = runTest {
        coEvery { getAllTasksUseCase.execute() } returns listOf(task)
        coEvery { TaskCliUtils.fetchProjectTasks(any(), any(), any()) } returns listOf(task)
        coEvery { TaskCliUtils.selectTask(any(), any()) } returns null

        updateTaskCli.update(projectId)

        coVerify(exactly = 0) { updateTaskUseCase.execute(any(), any(), any(), any(), any(), any(), any()) }
    }
}