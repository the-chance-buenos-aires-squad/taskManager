package presentation.cli.TaskState

import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper

class TaskStateCliControllerTest {

    private val projectCliHelper: ProjectCliHelper = mockk(relaxed = true)
    private val createTaskStateCli: CreateTaskStateCli = mockk(relaxed = true)
    private val editTaskStateCli: EditTaskStateCli = mockk(relaxed = true)
    private val deleteTaskStateCli: DeleteTaskStateCli = mockk(relaxed = true)
    private val getAllTaskStatesCli: GetAllTaskStatesCli = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)

    private val controller = TaskStateCliController(
        projectCliHelper,
        createTaskStateCli,
        editTaskStateCli,
        deleteTaskStateCli,
        getAllTaskStatesCli,
        uiController
    )

    @Test
    fun `should print no-projects message when no projects availabl`() {
        // given
        every { projectCliHelper.getProjects() } returns emptyList()

        // when
        controller.start()

        // then
        verify { uiController.printMessage("No Projects yet, try to create project") }
    }

    @Test
    fun `should print invalid project message when selectProject returns null`() {
        // given
        val fakeProjects = listOf(createDummyProject(name = "X", description = ""))
        every { projectCliHelper.getProjects() } returns fakeProjects
        every { projectCliHelper.selectProject(fakeProjects) } returns null

        // when
        controller.start()

        // then
        verify { uiController.printMessage("invalid project") }
    }

    @Test
    fun `should invoke createTaskState when user selects option 1`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        // first readInput() returns "1", next throws to break loop
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("exit")

        // when
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { createTaskStateCli.createTaskState(projectId = project.id) }
    }

    @Test
    fun `should invoke editTaskState when user selects option 2`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "2" andThenThrows RuntimeException("exit")

        // when 
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { editTaskStateCli.editTaskState(project.id) }
    }

    @Test
    fun `should invoke deleteTaskState when user selects option 3`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "3" andThenThrows RuntimeException("exit")

        // when 
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { deleteTaskStateCli.deleteTaskState() }
    }

    @Test
    fun `should invoke getAllTaskStates when user selects option 4`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "4" andThenThrows RuntimeException("exit")

        // when
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { getAllTaskStatesCli.getAllTaskStates() }
    }

    @Test
    fun `should return without exception when user selects option 5`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "5"

        // when & then
        controller.start()
    }

    @Test
    fun `should print empty-input message when user enters empty input`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "" andThenThrows RuntimeException("exit")

        // when
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { uiController.printMessage(ProjectCliHelper.EMPTY_INPUT_MESSAGE) }
    }

    @Test
    fun `should print invalid-input message when user enters empty input`() {
        // given
        val project = createDummyProject(name = "P", description = "")
        every { projectCliHelper.getProjects() } returns listOf(project)
        every { projectCliHelper.selectProject(any()) } returns project
        every { uiController.readInput() } returns "99" andThenThrows RuntimeException("exit")

        // when
        assertThrows<RuntimeException> { controller.start() }

        // then
        verify { uiController.printMessage(ProjectCliHelper.INVALID_INPUT_MESSAGE) }
    }
}