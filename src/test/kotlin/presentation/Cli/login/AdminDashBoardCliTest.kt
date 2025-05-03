import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.Cli.CreateUserCli
import presentation.Cli.login.AdminDashBoardCli
import presentation.UiController
import java.util.*

class AdminDashBoardCliTest {

    private lateinit var adminDashBoardCli: AdminDashBoardCli
    private val uiController: UiController = mockk(relaxed = true)
    private val createUserCli: CreateUserCli = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        adminDashBoardCli = AdminDashBoardCli(uiController, createUserCli)
    }

    @Test
    fun `should display message when start admin cli`() {
        // given:
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify {
            uiController.printMessage(
                " === Admin Dashboard ===\n" +
                        " 1. Create User Mate\n" +
                        " 2. Manage Project\n" +
                        "3. Manage task States\n" +
                        " 4. View Audit Logs\n" +
                        " 5. Logout\n" +
                        " Choose an option (1-5):")
        }
    }


    @Test
    fun `should start createUserCli when user choose option 1`() {
        // given
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { createUserCli.start() }
    }

    @Test
    fun `should start manage project cli when user choose option 2`() {
        // given
        every { uiController.readInput() } returns "2" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Manage Project") }
    }

    @Test
    fun `should start manageTaskStatesCli when user choose option 3`() {
        // given
        every { uiController.readInput() } returns "3" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Manage task States") }
    }

    @Test
    fun `should start ViewAuditLogsCli when user choose option 4`() {
        // given
        every { uiController.readInput() } returns "4" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("View Audit Logs") }
    }

    @Test
    fun `should start Logout when user choose option 5`() {
        // given
        every { uiController.readInput() } returns "5" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Logout") }
    }

    @Test
    fun `should restart adminDashBoardCli when invalid input`() {
        // given
        every { uiController.readInput() } returns "99" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Invalid option!") }
    }
}