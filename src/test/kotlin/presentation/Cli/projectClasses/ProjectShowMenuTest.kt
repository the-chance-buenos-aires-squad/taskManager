package presentation.Cli.projectClasses

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class ProjectShowMenuTest {
    private lateinit var uiController: UiController
    private lateinit var projectShowMenu: ProjectShowMenu

    @BeforeEach
    fun setup() {
        uiController = mockk(relaxed = true)
        projectShowMenu = ProjectShowMenu(uiController)
    }

    @Test
    fun `should print menu and call input handler when call show menu function`() {
        projectShowMenu.showMenu()

        verify { uiController.printMessage(any()) }
    }
}