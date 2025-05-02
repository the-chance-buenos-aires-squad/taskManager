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
    fun `selecting option 1 calls createUserCli start`() {
        // Given: Simulate user input "1" then throw exception to exit loop
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify {
            uiController.printMessage(
                " === Admin Dashboard ===\n" +
                        " 1. Create User Mate\n" +
                        " 2. Manage Project\n" +
                        "3. Manage task States\n" +
                        " 4. View Audit Logs\n" +
                        " 5. Logout\n" +
                        " Choose an option (1-5):")
            createUserCli.start()
        }
    }

    @Test
    fun `selecting option 2 prints manage project message`() {
        // Given
        every { uiController.readInput() } returns "2" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify { uiController.printMessage("Manage Project") }
    }

    @Test
    fun `selecting option 3 prints manage task states message`() {
        // Given
        every { uiController.readInput() } returns "3" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify { uiController.printMessage("Manage task States") }
    }

    @Test
    fun `selecting option 4 prints view audit logs message`() {
        // Given
        every { uiController.readInput() } returns "4" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify { uiController.printMessage("View Audit Logs") }
    }

    @Test
    fun `selecting option 5 prints logout message`() {
        // Given
        every { uiController.readInput() } returns "5" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify { uiController.printMessage("Logout") }
    }

    @Test
    fun `selecting invalid option prints error message`() {
        // Given
        every { uiController.readInput() } returns "99" andThenThrows RuntimeException("Exit loop")

        // When
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // Then
        verify { uiController.printMessage("Invalid option!") }
    }
}