import presentation.Cli.login.MateDashBoardCli

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController

class MateDashBoardCliTest {
    private lateinit var mateDashBoardCli: MateDashBoardCli
    private val uiController: UiController = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        mateDashBoardCli = MateDashBoardCli(uiController)
    }

    @Test
    fun `should display message when start mate dashboard cli`() {
        // given:
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify {
            uiController.printMessage(
                " === Mate Dashboard ===\n" +
                        " 1. Manage Task\n" +
                        " 2. View Swimlanes\n" +
                        " 3. View Audit Logs\n" +
                        " 4. Logout\n" +
                        " Choose an option (1-4):"
            )
        }
    }


    @Test
    fun `should start manageTaskCli when user choose option 1`() {
        // given
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Manage Task") }
    }

    @Test
    fun `should start ViewSwimlanesCli when user choose option 2`() {
        // given
        every { uiController.readInput() } returns "2" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("View Swimlanes") }
    }

    @Test
    fun `should start ViewAuditLogsCli when user choose option 3`() {
        // given
        every { uiController.readInput() } returns "3" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("View Audit Logs") }
    }

    @Test
    fun `should logout when user choose option 4`() {
        // given
        every { uiController.readInput() } returns "4" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Logout") }
    }



    @Test
    fun `should restart adminDashBoardCli when invalid input`() {
        // given
        every { uiController.readInput() } returns "9" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Invalid option!") }
    }
}