package presentation.cli.dashBoard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.task.ViewSwimlanesCLI

class MateDashBoardCliTest {
    private val uiController: UiController = mockk(relaxed = true)
    private val viewSwimlanesCLI: ViewSwimlanesCLI = mockk(relaxed = true)
    private lateinit var mateDashBoardCli: MateDashBoardCli


    @BeforeEach
    fun setUp() {
        mateDashBoardCli = MateDashBoardCli(uiController, viewSwimlanesCLI)
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
                        " 1. View Swimlanes\n" +
                        " 2. Logout\n" +
                        " Choose an option (1-2):"
            )
        }
    }


    @Test
    fun `should start ViewSwimlanesCli when user choose option 1`() {
        // given
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }
        // then
        verify { viewSwimlanesCLI.start() }
    }

    @Test
    fun `should logout when user choose option 2`() {
        // given
        every { uiController.readInput() } returns "2"

        // when
        mateDashBoardCli.start()

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

    @Test
    fun `should restart mateDashBoardCli when empty input`() {
        // given
        every { uiController.readInput() } returns "" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { mateDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Invalid option!") }
    }
}