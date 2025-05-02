import domain.entities.User
import domain.entities.UserRole
import domain.usecases.AuthenticationUseCase
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.login.AdminDashBoardCli
import presentation.Cli.login.LoginCli
import presentation.Cli.login.MateDashBoardCli
import presentation.UiController
import java.util.*
import kotlin.test.assertEquals

class LoginCliTest {

    private lateinit var loginCli: LoginCli
    private val uiController: UiController = mockk(relaxed = true)
    private val authenticationUseCase: AuthenticationUseCase = mockk()
    private val adminDashBoardCli: AdminDashBoardCli = mockk(relaxed = true)
    private val mateDashBoardCli: MateDashBoardCli = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        loginCli = LoginCli(uiController, authenticationUseCase, adminDashBoardCli, mateDashBoardCli)
    }

    @Test
    fun `should login successfully as admin and redirect to admin dashboard`() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "admin",
            password = "hashedPass",
            role = UserRole.ADMIN,
            createdAt = mockk()
        )

        every { uiController.readInput() } returnsMany listOf("admin", "password")
        every { authenticationUseCase.login("admin", "password") } returns user

        // When
        loginCli.start()

        // Then
        verify {
            uiController.printMessage("\n=== Login ===")
            uiController.printMessage("Username: ")
            uiController.printMessage("Password: ")
            uiController.printMessage("\nWelcome admin!")
            adminDashBoardCli.start()
        }
    }

    @Test
    fun `should login successfully as mate and redirect to mate dashboard`() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "mate",
            password = "hashedPass",
            role = UserRole.MATE,
            createdAt = mockk()
        )

        every { uiController.readInput() } returnsMany listOf("mate", "password")
        every { authenticationUseCase.login("mate", "password") } returns user

        // When
        loginCli.start()

        // Then
        verify {
            uiController.printMessage("\n=== Login ===")
            uiController.printMessage("Username: ")
            uiController.printMessage("Password: ")
            uiController.printMessage("\nWelcome mate!")
            mateDashBoardCli.start()
        }
    }

    @Test
    fun `should show error message on invalid credentials`() {
        // Given
        val exception = Exception("Invalid username or password")

        every { uiController.readInput() } returnsMany listOf("wrongUser", "wrongPass")
        every { authenticationUseCase.login("wrongUser", "wrongPass") } throws exception

        // When
        loginCli.start()

        // Then
        verify {
            uiController.printMessage("Error: Invalid username or password")
        }
    }

    @Test
    fun `should handle unexpected errors during login`() {
        // Given
        val exception = RuntimeException("Database error")

        every { uiController.readInput() } returnsMany listOf("user", "pass")
        every { authenticationUseCase.login("user", "pass") } throws exception

        // When
        loginCli.start()

        // Then
        verify {
            uiController.printMessage("Error: Database error")
        }
    }

    @Test
    fun `should return after 2 failed attempts`() {
        // given
        every { authenticationUseCase.login(any(), any()) } throws Exception("Invalid credentials")
        every { uiController.readInput() } returnsMany listOf("user1", "pass1", "user2", "pass2")

        // When
        loginCli.start()

        // Then
        verify(exactly = 1) { uiController.printMessage("Too many failed attempts. Returning to main menu.") }
    }
}