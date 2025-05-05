package presentation.cli.auth

import domain.customeExceptions.InvalidCredentialsException
import domain.usecases.AuthenticationUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

class LoginCliTest {
    private lateinit var loginCli: LoginCli
    private val uiController: UiController = mockk(relaxed = true)
    private val authenticationUseCase: AuthenticationUseCase = mockk()
    private val adminDashBoardCli: AdminDashBoardCli = mockk(relaxed = true)
    private val mateDashBoardCli: MateDashBoardCli = mockk(relaxed = true)
    val adminTestUser = DummyUser.dummyUserOne
    val mateTestUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        loginCli = LoginCli(uiController, authenticationUseCase, adminDashBoardCli, mateDashBoardCli)
    }

    @Test
    fun `should login successfully as admin and redirect to admin dashboard`() {
        // Given
        every { uiController.readInput() } returnsMany listOf(adminTestUser.username, adminTestUser.password)
        every { authenticationUseCase.login(adminTestUser.username, adminTestUser.password) } returns adminTestUser

        // When
        loginCli.start()

        // Then
        verify {
            adminDashBoardCli.start()
        }
    }

    @Test
    fun `should login successfully as mate and redirect to mate dashboard`() {
        // Given

        every { uiController.readInput() } returnsMany listOf(mateTestUser.username, mateTestUser.password)
        every { authenticationUseCase.login(mateTestUser.username, mateTestUser.password) } returns mateTestUser

        // When
        loginCli.start()

        // Then
        verify {
            mateDashBoardCli.start()
        }
    }

    @Test
    fun `should show error message on invalid credentials`() {
        // Given
        val exception = InvalidCredentialsException()

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
        val exception = Exception("unexpected errors")

        every { uiController.readInput() } returnsMany listOf("user", "pass")
        every { authenticationUseCase.login("user", "pass") } throws exception

        // When
        loginCli.start()

        // Then
        verify {
            uiController.printMessage("Error: unexpected errors")
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