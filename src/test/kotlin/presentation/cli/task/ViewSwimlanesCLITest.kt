package presentation.cli.task

import createDummyTaskState
import domain.entities.TaskState
import domain.entities.TaskStateWithTasks
import domain.usecases.GetTasksGroupedByStateUseCase
import dummyData.createDummyProject
import dummyData.createDummyTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.helper.ProjectCliHelper.Companion.EMPTY_INPUT_MESSAGE
import presentation.cli.task.ViewSwimlanesCLI.Companion.DISPLAY_OPTION_MANAGE_TASK
import java.util.UUID
import javax.swing.text.View

class ViewSwimlanesCLITest {

    private val uiController: UiController = mockk(relaxed = true)
    private val projectCliHelper: ProjectCliHelper= mockk(relaxed = true)
    private val getTasksGroupedByStateUseCase: GetTasksGroupedByStateUseCase=mockk(relaxed = true)
    private val createTaskCli: CreateTaskCli = mockk(relaxed = true)
    private val viewSwimlanesCLI : ViewSwimlanesCLI = ViewSwimlanesCLI(uiController,projectCliHelper,getTasksGroupedByStateUseCase,createTaskCli)

    private val sampleProject = createDummyProject(
        id = UUID.randomUUID(),
        name = "dummy project"
    )
    private val swimlanes = listOf(
        TaskStateWithTasks(
            state = createDummyTaskState(
                id = "1",
                name = "in progress",
                projectId = sampleProject.id.toString()
            ),
            tasks = listOf(
                createDummyTask(
                    title = "task title",
                    description = "task desc",
                    projectId = sampleProject.id,
                    assignedTo = null,
                    stateId = "1"
                )
            )
        ),
        TaskStateWithTasks(
            state = createDummyTaskState(
                id = "2",
                name = "Done",
                projectId = sampleProject.id.toString()
            ),
            tasks = emptyList()
        )
    )


    @Test
    fun `should display welcome message when start viewSwimLanceCli`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify {
            uiController.printMessage(
                "========================================\n" +
                        "         TASK SWIMLANES VIEW             \n" +
                        "========================================\n"
            )
        }
    }

    @Test
    fun `should return to mate dashboard when project is not found`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns null
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify {
            uiController.printMessage("invalid project")
        }
    }

    @Test
    fun `should start manage task when valid project`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage(DISPLAY_OPTION_MANAGE_TASK)
        }
    }

    @Test
    fun `should start create task when user select 1`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "1" andThen "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify(exactly = 1) {
            createTaskCli.start(sampleProject.id)
        }
    }

    @Test
    fun `should start update task cli when user select 2`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "2" andThen "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify (exactly = 1){
            uiController.printMessage("update task cli")
        }
    }

    @Test
    fun `should start delete task cli when user select 3`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "3" andThen "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage("delete task cli")
        }
    }

    @Test
    fun `should back to menu when user enter empty`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "" andThen "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify(exactly = 1){
            uiController.printMessage("Invalid input: please enter a valid number.")
        }
    }

    @Test
    fun `should back to menu when user enter invalid choose`() {
        // given
        every { projectCliHelper.getProjects() } returns listOf(sampleProject)
        every { projectCliHelper.selectProject(any()) } returns sampleProject
        every { getTasksGroupedByStateUseCase.getTasksGroupedByState(sampleProject) } returns swimlanes
        every { uiController.readInput() } returns "8" andThen "4"

        //when
        viewSwimlanesCLI.start()

        // then
        verify(exactly = 1) {
            uiController.printMessage("Invalid Input: please enter a valid number from the menu.")
        }
    }
}