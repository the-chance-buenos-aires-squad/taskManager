package presentation.cli.task

import createDummyTaskState
import domain.usecases.groupingByState.GetTasksGroupedByStateUseCase
import dummyData.createDummyProject
import dummyData.createDummyTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.task.ViewSwimlanesCLI.Companion.DISPLAY_OPTION_MANAGE_TASK
import java.util.*

class ViewSwimlanesCLITest {

    private val uiController: UiController = mockk(relaxed = true)
    private val projectCliHelper: ProjectCliHelper = mockk(relaxed = true)
    private val getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase = mockk(relaxed = true)
    private val createTaskCli: CreateTaskCli = mockk(relaxed = true)
    private val updateTaskCli: UpdateTaskCli = mockk(relaxed = true)
    private val deleteTaskCli: DeleteTaskCli = mockk(relaxed = true)
    private val viewSwimlanesCLI: ViewSwimlanesCLI = ViewSwimlanesCLI(
        uiController,
        projectCliHelper,
        getTasksGroupedByStateUseCase,
        createTaskCli,
        updateTaskCli,
        deleteTaskCli
    )

    private val sampleProject = createDummyProject(
        id = UUID.randomUUID(),
        name = "dummy project"
    )
    private val stateId = UUID.randomUUID()
    private val swimlanes = listOf(
        GetTasksGroupedByStateUseCase.TaskStateWithTasks(
            state = createDummyTaskState(
                id = stateId,
                name = "in progress",
                projectId = sampleProject.id
            ),
            tasks = listOf(
                createDummyTask(
                    title = "task title",
                    description = "task desc",
                    projectId = sampleProject.id,
                    assignedTo = null,
                    stateId = stateId
                )
            )
        ),
        GetTasksGroupedByStateUseCase.TaskStateWithTasks(
            state = createDummyTaskState(
                id = UUID.randomUUID(),
                name = "Done",
                projectId = sampleProject.id
            ),
            tasks = emptyList()
        )
    )

    @Test
    fun `should display welcome message when start viewSwimLanceCli`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "4"

        viewSwimlanesCLI.start()

        coVerify {
            uiController.printMessage(
                "========================================\n" +
                        "         TASK SWIMLANES VIEW             \n" +
                        "========================================\n"
            )
        }
    }

    @Test
    fun `should return to mate dashboard when project is not found`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns null
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "4"

        viewSwimlanesCLI.start()

        coVerify {
            uiController.printMessage("invalid project")
        }
    }

    @Test
    fun `should start manage task when valid project`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "4"

        viewSwimlanesCLI.start()

        coVerify(exactly = 1) {
            uiController.printMessage(DISPLAY_OPTION_MANAGE_TASK)
        }
    }
//TODO fix
//    @Test
//    fun `should start create task when user select 1`() = runTest {
//        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
//        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
//        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
//        coEvery { uiController.readInput() } returns "1" andThen "4"
//
//        viewSwimlanesCLI.start()
//
//        coVerify(exactly = 1) {
//            createTaskCli.create(sampleProject.id)
//        }
//    }

    @Test
    fun `should start update task cli when user select 2`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "2" andThen "4"

        viewSwimlanesCLI.start()

        // then
        coVerify(exactly = 1) {
            updateTaskCli.update(any())
        }
    }

    @Test
    fun `should start delete task cli when user select 3`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "3" andThen "4"

        viewSwimlanesCLI.start()

        // then
        coVerify(exactly = 1) {
            deleteTaskCli.delete(sampleProject.id)
        }
    }

    @Test
    fun `should back to menu when user enter empty`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "" andThen "4"

        viewSwimlanesCLI.start()

        coVerify(exactly = 1) {
            uiController.printMessage("Invalid input: please enter a valid number.")
        }
    }

    @Test
    fun `should back to menu when user enter invalid choose`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        coEvery { projectCliHelper.selectProject(any()) } returns sampleProject
        coEvery { getTasksGroupedByStateUseCase.execute(sampleProject) } returns swimlanes
        coEvery { uiController.readInput() } returns "8" andThen "4"

        viewSwimlanesCLI.start()

        coVerify(exactly = 1) {
            uiController.printMessage("Invalid Input: please enter a valid number from the menu.")
        }
    }
}