package presentation.Cli.projectClasses

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class ProjectScreenControllerTest {
    private lateinit var projectShowMenu: ProjectShowMenu
    private lateinit var projectScreenController: ProjectScreenController
    private lateinit var projectActionHandler: ProjectActionHandler
    private lateinit var uiController: UiController

    @BeforeEach
    fun setup() {
        projectShowMenu = mockk(relaxed = true)
        projectActionHandler = mockk(relaxed = true)
        uiController = mockk(relaxed = true)
        projectScreenController = ProjectScreenController(projectShowMenu, projectActionHandler, uiController)
    }

    @Test
    fun `should call create project function when user input number one`() {
        every { projectShowMenu.showMenu() } returns 1 andThen 4

        projectScreenController.show()

        verify { projectActionHandler.create() }
    }

    @Test
    fun `should call edit project function when user input number tow`() {
        every { projectShowMenu.showMenu() } returns 2 andThen 4

        projectScreenController.show()

        verify { projectActionHandler.edit() }
    }

    @Test
    fun `should call delete project function when user input number three`() {
        every { projectShowMenu.showMenu() } returns 3 andThen 4

        projectScreenController.show()

        verify { projectActionHandler.delete() }
    }

    @Test
    fun `should print Invalid Input when user input invalid value`() {
        every { projectShowMenu.showMenu() } returns null andThen 4

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number.") }
    }

    @Test
    fun `should print Invalid Input when user input invalid value and`() {
        every { projectShowMenu.showMenu() } returns 7 andThen 4

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number in menu.") }
    }
}