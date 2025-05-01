package presentation.Cli.projectClasses

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.InputHandler

class ProjectShowMenuTest {
    private lateinit var inputHandler: InputHandler
    private lateinit var projectShowMenu: ProjectShowMenu

    @BeforeEach
    fun setup() {
        inputHandler = mockk(relaxed = true)
        projectShowMenu = ProjectShowMenu(inputHandler)
    }

    @Test
    fun `should print menu and call input handler when call show menu function`() {
        projectShowMenu.showMenu()
        verify { inputHandler.readInt(any()) }
    }
}