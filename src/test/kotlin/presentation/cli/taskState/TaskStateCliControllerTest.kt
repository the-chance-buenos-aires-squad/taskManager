package presentation.cli.taskState

import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import java.util.*

class TaskStateCliControllerTest {

    private val projectCliHelper: ProjectCliHelper = mockk(relaxed = true)
    private val createTaskStateCli: CreateTaskStateCli = mockk(relaxed = true)
    private val editTaskStateCli: EditTaskStateCli = mockk(relaxed = true)
    private val deleteTaskStateCli: DeleteTaskStateCli = mockk(relaxed = true)
    private val getAllTaskStatesCli: GetAllTaskStatesCli = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val taskStateCliController: TaskStateCliController = TaskStateCliController(
        projectCliHelper,
        createTaskStateCli,
        editTaskStateCli,
        deleteTaskStateCli,
        getAllTaskStatesCli,
        uiController
    )

    private val sampleProject = createDummyProject(
        id = UUID.randomUUID(),
        name = "dummy project"
    )


    @Test
    fun `should display welcome message when start taskStateCliController`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "5"

        //when
        taskStateCliController.start()

        // then
        verify {
            uiController.printMessage(
                "==============================\n" +
                        "    Task State Management     \n" +
                        "===============================\n"
            )
        }
    }

    @Test
    fun `should return to admin dashboard when projects is not found`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns emptyList()
        every { projectCliHelper.selectProject(any()) } returns null
        every { uiController.readInput() } returns "5"

        //when
        taskStateCliController.start()

        // then
        verify {
            uiController.printMessage("No Projects yet, try to create project")
        }
    }

    @Test
    fun `should return to admin dashboard when project is not found`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns null
        every { uiController.readInput() } returns "5"

        //when
        taskStateCliController.start()

        // then
        verify {
            uiController.printMessage("invalid project")
        }
    }

    @Test
    fun `should start manage task when valid project`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage(
                "================================\n" +
                        " 1. Create Task State       \n" +
                        " 2. Edit Task State         \n" +
                        " 3. Delete Task State       \n" +
                        " 4. View All Task States    \n" +
                        " 5. Back to Main Menu       \n" +
                        "==============================\n" +
                        "Choose an option (1-5):"
            )
        }
    }

    @Test
    fun `should start create task state when user select 1`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "1" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            createTaskStateCli.createTaskState(sampleProject.id)
        }
    }

    @Test
    fun `should start update task state cli when user select 2`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "2" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            editTaskStateCli.editTaskState(sampleProject.id)
        }
    }

    @Test
    fun `should start delete task state cli when user select 3`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "3" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            deleteTaskStateCli.deleteTaskState(any())
        }
    }

    @Test
    fun `should start show tasks state cli when user select 4`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "4" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            getAllTaskStatesCli.getAllTaskStates(any())
        }
    }

    @Test
    fun `should back to menu when user enter empty`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage("Invalid input: please enter a valid number.")
        }
    }

    @Test
    fun `should back to menu when user enter invalid choose`() = runTest{
        // given
        coEvery { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { uiController.readInput() } returns "8" andThen "5"

        //when
        taskStateCliController.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage("Invalid Input: please enter a valid number from the menu.")
        }
    }

}