package presentation.cli.auth

import data.exceptions.InvalidCredentialsException
import domain.usecases.AuthenticationUseCase
import domain.validation.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

class LoginCliTest {
    private lateinit var loginCli: LoginCli
    private val uiController: UiController = mockk(relaxed = true)
    private val authenticationUseCase: AuthenticationUseCase = mockk()
    private val userValidator : UserValidator = mockk(relaxed = true)
    private val adminDashBoardCli: AdminDashBoardCli = mockk(relaxed = true)
    private val mateDashBoardCli: MateDashBoardCli = mockk(relaxed = true)
    private val userValidator:UserValidator = mockk(relaxed = true)
    val adminTestUser = DummyUser.dummyUserOne
    val mateTestUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        loginCli = LoginCli(uiController, authenticationUseCase, adminDashBoardCli, mateDashBoardCli,userValidator)
    }

    @Test
    fun `should login successfully as admin and redirect to admin dashboard`() = runTest {
        // Given
        coEvery { uiController.readInput() } returnsMany listOf(adminTestUser.username, "adminPassword")
        coEvery { authenticationUseCase.login(adminTestUser.username, "adminPassword") } returns adminTestUser

        // When
        loginCli.start()

        // Then
        coVerify {
            adminDashBoardCli.start()
        }
    }

    @Test
    fun `should login successfully as mate and redirect to mate dashboard`() = runTest {
        // Given
        coEvery { uiController.readInput() } returnsMany listOf(mateTestUser.username, "adminPassword")
        coEvery { authenticationUseCase.login(mateTestUser.username, "adminPassword") } returns mateTestUser

        // When
        loginCli.start()

        // Then
        coVerify {
            mateDashBoardCli.start()
        }
    }

    @Test
    fun `should show error message on invalid credentials`() = runTest {
        // Given
        val exception = InvalidCredentialsException()

        coEvery { uiController.readInput() } returnsMany listOf("wrongUser", "wrongPass")
        coEvery { authenticationUseCase.login("wrongUser", "wrongPass") } throws exception

        // When
        loginCli.start()

        // Then
        coVerify {
            uiController.printMessage("Error: Invalid username or password")
        }
    }

    @Test
    fun `should handle unexpected errors during login`() = runTest {
        // Given
        val exception = Exception("unexpected errors")

        coEvery { uiController.readInput() } returnsMany listOf("user", "pass")
        coEvery { authenticationUseCase.login("user", "pass") } throws exception

        // When
        loginCli.start()

        // Then
        coVerify {
            uiController.printMessage("Error: unexpected errors")
        }
    }

    @Test
    fun `should return after 2 failed attempts`() = runTest {
        // Given
        coEvery { authenticationUseCase.login(any(), any()) } throws Exception("Invalid credentials")
        coEvery { uiController.readInput() } returnsMany listOf("user1", "pass1", "user2", "pass2")

        // When
        loginCli.start()

        // Then
        coVerify(exactly = 1) { uiController.printMessage("Too many failed attempts. Returning to main menu.") }
    }
}