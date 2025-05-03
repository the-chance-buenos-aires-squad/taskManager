package presentation.cli.project

import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class ProjectScreenControllerTest {
    private lateinit var projectShowMenu: ProjectShowMenu
    private lateinit var projectScreenController: ProjectScreenController
    private lateinit var createProjectCli: CreateProjectCli
    private lateinit var updateProjectCli: UpdateProjectCli
    private lateinit var deleteProjectCli: DeleteProjectCli
    private lateinit var uiController: UiController

    @BeforeEach
    fun setup() {
        projectShowMenu = mockk(relaxed = true)
        createProjectCli = mockk(relaxed = true)
        updateProjectCli = mockk(relaxed = true)
        deleteProjectCli = mockk(relaxed = true)
        uiController = mockk(relaxed = true)
        projectScreenController = ProjectScreenController(
            projectShowMenu,
            createProjectCli,
            updateProjectCli,
            deleteProjectCli,
            uiController
        )
    }

    @Test
    fun `should call create project function when user input number one`() {
        every { uiController.readInput() } returns "1" andThen "4"

        projectScreenController.show()

        verify { createProjectCli.create() }
    }

    @Test
    fun `should handle createProjectCli with UserEnterInvalidValueException`() {
        every { uiController.readInput() } returns "1" andThen "4"
        every { createProjectCli.create() } throws UserEnterInvalidValueException("Invalid Input")

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input") }
    }

    @Test
    fun `should call edit project function when user input number tow`() {
        every { uiController.readInput() } returns "2" andThen "4"

        projectScreenController.show()

        verify { updateProjectCli.update() }
    }

    @Test
    fun `should handle updateProjectCli with UserEnterInvalidValueException`() {
        every { uiController.readInput() } returns "2" andThen "4"
        every { updateProjectCli.update() } throws UserEnterInvalidValueException("Invalid update")
        projectScreenController.show()
        verify { uiController.printMessage("Invalid update") }
    }

    @Test
    fun `should handle updateProjectCli with NoProjectsFoundException`() {
        every { uiController.readInput() } returns "2" andThen "4"
        every { updateProjectCli.update() } throws NoProjectsFoundException("No projects")
        projectScreenController.show()
        verify { uiController.printMessage("No projects") }
    }

    @Test
    fun `should call delete project function when user input number three`() {
        every { uiController.readInput() } returns "3" andThen "4"

        projectScreenController.show()

        verify { deleteProjectCli.delete() }
    }

    @Test
    fun `should handle deleteProjectCli with UserEnterInvalidValueException`() {
        every { uiController.readInput() } returns "3" andThen "4"
        every { deleteProjectCli.delete() } throws UserEnterInvalidValueException("Can't delete")
        projectScreenController.show()
        verify { uiController.printMessage("Can't delete") }
    }

    @Test
    fun `should handle deleteProjectCli with NoProjectsFoundException`() {
        every { uiController.readInput() } returns "3" andThen "4"
        every { deleteProjectCli.delete() } throws NoProjectsFoundException("Empty list")
        projectScreenController.show()
        verify { uiController.printMessage("Empty list") }
    }

    @Test
    fun `should handel Invalid Input when user input empty value`() {
        every { uiController.readInput() } returns "" andThen "4"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number.") }
    }

    @Test
    fun `should handel Invalid Input when user input invalid value`() {
        every { uiController.readInput() } returns "7" andThen "4"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number in menu.") }
    }

    @Test
    fun `should handel Invalid Input when user input is return null`() {
        every { uiController.readInput() } returns "j" andThen "4"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number.") }
    }
}