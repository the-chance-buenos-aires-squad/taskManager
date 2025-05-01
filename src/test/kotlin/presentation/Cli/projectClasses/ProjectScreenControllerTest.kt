package presentation.Cli.projectClasses

import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.EditProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.InputHandler

class ProjectScreenControllerTest {
    private lateinit var projectShowMenu: ProjectShowMenu
    private lateinit var projectScreenController: ProjectScreenController
    private lateinit var projectActionHandler: ProjectActionHandler

    @BeforeEach
    fun setup() {
        projectShowMenu = mockk(relaxed = true)
        projectActionHandler = mockk(relaxed = true)
        projectScreenController = ProjectScreenController(projectShowMenu, projectActionHandler)
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
}